resource "kubernetes_namespace_v1" "techchallenge_ns" {

  metadata {
    name = "techchallenge"
  }
}

resource "kubectl_manifest" "postgres_service" {
  depends_on         = [kubernetes_namespace_v1.techchallenge_ns]
  override_namespace = "techchallenge"
  yaml_body          = file("../k8s/postgres-service.yaml")
}

resource "kubectl_manifest" "postgres_deployment" {
  depends_on         = [kubernetes_namespace_v1.techchallenge_ns]
  override_namespace = "techchallenge"
  yaml_body          = file("../k8s/postgres-deployment.yaml")
}

resource "kubectl_manifest" "secrets" {
  depends_on         = [kubernetes_namespace_v1.techchallenge_ns]
  override_namespace = "techchallenge"
  yaml_body          = file("../k8s/secret.yaml")
}

resource "kubectl_manifest" "configmap" {
  depends_on         = [kubernetes_namespace_v1.techchallenge_ns]
  override_namespace = "techchallenge"
  yaml_body          = file("../k8s/configmap.yml")
}

resource "kubectl_manifest" "app_deployment" {
  depends_on = [
    kubectl_manifest.secrets,
    kubectl_manifest.configmap,
    kubectl_manifest.postgres_deployment
  ]
  override_namespace = "techchallenge"
  yaml_body          = file("../k8s/app-deployment.yaml")
}


resource "kubectl_manifest" "app_service" {
  depends_on         = [kubectl_manifest.app_deployment]
  override_namespace = "techchallenge"
  yaml_body          = file("../k8s/app-service.yaml")
}

resource "kubectl_manifest" "app_hpa" {
  depends_on         = [kubectl_manifest.app_deployment]
  override_namespace = "techchallenge"
  yaml_body          = file("../k8s/hpa.yaml")
}

resource "kubectl_manifest" "ingress" {
  depends_on         = [kubectl_manifest.app_service]
  override_namespace = "techchallenge"
  yaml_body          = file("../k8s/ingress.yaml")
}

resource "null_resource" "app_port_forward" {
  depends_on = [kubectl_manifest.app_service]

  provisioner "local-exec" {
    when    = create
    command = <<EOT
      # 1. Comando para rodar em background usando Start-Process
      Start-Process kubectl -ArgumentList "port-forward svc/techchallenge-service 8080:8080 -n techchallenge"

      # 2. Pausa e Mensagem (usando comandos PowerShell)
      Start-Sleep -Seconds 5
      Write-Host "======================================================================="
      Write-Host "Porta 8080 habilitada!"
      Write-Host "Acesse o Swagger UI em: http://localhost:8080/swagger-ui/index.html"
      Write-Host "======================================================================="
    EOT
    interpreter = ["PowerShell", "-Command"]
  }
}