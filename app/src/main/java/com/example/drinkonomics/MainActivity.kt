package com.example.drinkonomics

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins

class MainActivity : AppCompatActivity() {
    private var contadorBebidas = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdicionar = findViewById<Button>(R.id.btnAdicionarBebida)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val containerBebidas = findViewById<LinearLayout>(R.id.containerBebidasAdicionais)
        val mainContainer = findViewById<LinearLayout>(R.id.mainContainer)

        // Adiciona título e instruções no topo
        val tvTituloApp = TextView(this).apply {
            text = "Drinkonomics"
            textSize = 24f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, dpParaPx(16), 0, dpParaPx(16))
            }
        }

        val tvInstrucoes = TextView(this).apply {
            text = "Informe os dados das bebidas:\n- Nome\n- Volume (ml)\n- Teor alcoólico (%)\n- Preço (R$)"
            textSize = 16f
            gravity = Gravity.START
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(dpParaPx(16), 0, dpParaPx(16), dpParaPx(16))
            }
        }

        // Insere no topo do container
        mainContainer.addView(tvTituloApp, 0)
        mainContainer.addView(tvInstrucoes, 1)

        // Configura os hints com unidades de medida
        findViewById<EditText>(R.id.etNomeBebida1).hint = "Nome da Bebida"
        findViewById<EditText>(R.id.etVolume1).hint = "Volume (ml)"
        findViewById<EditText>(R.id.etGrau1).hint = "Teor alcoólico (%)"
        findViewById<EditText>(R.id.etPreco1).hint = "Preço (R$)"

        btnAdicionar.setOnClickListener {
            contadorBebidas++
            if (contadorBebidas <= 10) {
                adicionarCamposBebida(containerBebidas, contadorBebidas)
            } else {
                Toast.makeText(this, "Máximo de 10 bebidas atingido", Toast.LENGTH_SHORT).show()
            }
        }

        btnCalcular.setOnClickListener {
            calcularMelhorBebida(tvResultado)
        }

        // Adiciona rodapé com créditos
        val tvCredito = TextView(this).apply {
            textSize = 14f
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, dpParaPx(24), 0, dpParaPx(16))
            }

            // Faz o email clicável
            movementMethod = LinkMovementMethod.getInstance()
            val textoCompleto = "Desenvolvido por R. Douglas G. Aquino\nContato: aquinordga@gmail.com"
            val textoSpannable = SpannableString(textoCompleto)

            // Encontra a posição do email no texto
            val inicioEmail = textoCompleto.indexOf("aquinordga@gmail.com")
            val fimEmail = inicioEmail + "aquinordga@gmail.com".length

            // Cria um link clicável para o email
            textoSpannable.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:aquinordga@gmail.com")
                        }
                        startActivity(intent)
                    }
                },
                inicioEmail,
                fimEmail,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            text = textoSpannable
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.darker_gray))
            setTypeface(null, Typeface.ITALIC)
        }

        mainContainer.addView(tvCredito)
    }

    private fun adicionarCamposBebida(container: LinearLayout, numeroBebida: Int) {
        val bebidaLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 0, 0, dpParaPx(16)) }
            background = ContextCompat.getDrawable(this@MainActivity, R.drawable.border_layout)
            setPadding(dpParaPx(8), dpParaPx(8), dpParaPx(8), dpParaPx(8))
        }

        val headerLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            gravity = Gravity.CENTER_VERTICAL
        }

        val tvTitulo = TextView(this).apply {
            text = "Bebida $numeroBebida"
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val btnRemover = ImageButton(this).apply {
            setImageResource(android.R.drawable.ic_delete)
            background = null
            layoutParams = LinearLayout.LayoutParams(dpParaPx(48), dpParaPx(48))
            setOnClickListener {
                container.removeView(bebidaLayout)
                contadorBebidas--
            }
        }

        val etNome = criarEditText("Nome da Bebida", "etNomeBebida$numeroBebida")
        val etVolume = criarEditText("Volume (ml)", "etVolume$numeroBebida", InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        val etGrau = criarEditText("Teor alcoólico (%)", "etGrau$numeroBebida", InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        val etPreco = criarEditText("Preço (R$)", "etPreco$numeroBebida", InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)

        headerLayout.addView(tvTitulo)
        headerLayout.addView(btnRemover)
        bebidaLayout.addView(headerLayout)
        bebidaLayout.addView(etNome)
        bebidaLayout.addView(etVolume)
        bebidaLayout.addView(etGrau)
        bebidaLayout.addView(etPreco)
        container.addView(bebidaLayout)
    }

    private fun calcularMelhorBebida(tvResultado: TextView) {
        try {
            val bebidas = mutableListOf<Triple<String, Double, Double>>()

            for (i in 1..contadorBebidas) {
                val (nome, volume, grau, preco) = if (i == 1) {
                    Quadruple(
                        findViewById<EditText>(R.id.etNomeBebida1).text.toString(),
                        findViewById<EditText>(R.id.etVolume1).text.toString().toDoubleOrNull() ?: 0.0,
                        findViewById<EditText>(R.id.etGrau1).text.toString().toDoubleOrNull() ?: 0.0,
                        findViewById<EditText>(R.id.etPreco1).text.toString().toDoubleOrNull() ?: 1.0
                    )
                } else {
                    val container = findViewById<LinearLayout>(R.id.containerBebidasAdicionais).getChildAt(i - 2)
                    Quadruple(
                        container.findViewWithTag<EditText>("etNomeBebida$i")?.text.toString(),
                        container.findViewWithTag<EditText>("etVolume$i")?.text.toString().toDoubleOrNull() ?: 0.0,
                        container.findViewWithTag<EditText>("etGrau$i")?.text.toString().toDoubleOrNull() ?: 0.0,
                        container.findViewWithTag<EditText>("etPreco$i")?.text.toString().toDoubleOrNull() ?: 1.0
                    )
                }

                if (nome.isNotEmpty() && volume > 0 && grau > 0 && preco > 0) {
                    bebidas.add(Triple(nome, (volume * grau / 100) / preco, preco))
                }
            }

            if (bebidas.isEmpty()) {
                tvResultado.text = "Preencha os dados de pelo menos uma bebida."
                return
            }

            val ranking = bebidas.sortedByDescending { it.second }
            val resultado = buildString {
                append("🏆 MELHOR CUSTO-BENEFÍCIO:\n")
                append("${ranking[0].first} - ${"%.2f".format(ranking[0].second)} ml álcool/real\n\n📊 RANKING:\n")
                ranking.forEachIndexed { idx, (nome, eficiencia, preco) ->
                    append("${idx + 1}. $nome: ${"%.2f".format(eficiencia)} ml/R\$ (R\$ ${"%.2f".format(preco)})\n")
                }
            }
            tvResultado.text = resultado

        } catch (e: Exception) {
            tvResultado.text = "Erro: Verifique os dados."
            e.printStackTrace()
        }
    }

    private fun criarEditText(hint: String, tag: String, inputType: Int = InputType.TYPE_CLASS_TEXT): EditText {
        return EditText(this).apply {
            this.hint = hint
            this.tag = tag
            this.inputType = inputType
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, dpParaPx(4), 0, 0) }
        }
    }

    private fun dpParaPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()
}

data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)