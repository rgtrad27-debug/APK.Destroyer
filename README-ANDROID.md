# Minhas Finanças — Android

Aplicativo Android independente que empacota a interface HTML original em um WebView local.

## Recursos incluídos
- Resumo mensal e anual, receitas, despesas e saldo.
- Gráfico por categoria e evolução.
- Cadastro, edição e exclusão de lançamentos.
- Senha local e autenticação biométrica nativa do Android.
- Armazenamento local no dispositivo via localStorage do WebView.

## Gerar APK de teste
O workflow **Build Minhas Finanças APK** gera um APK debug automaticamente após push nesta branch ou execução manual em Actions. O artefato é `app-debug.apk`.

## Observações
- O APK debug é para teste e não é assinado para distribuição pública.
- Os dados ficam no aparelho; limpar os dados do app ou desinstalá-lo pode apagá-los.
- A biometria usa a API nativa do Android e requer biometria ou credencial de bloqueio configurada.
