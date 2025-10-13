provider "kubernetes" {
  # Força o uso do arquivo de configuração padrão do kubectl/Minikube.
  config_path    = "~/.kube/config"

  # Mantemos o contexto para garantir que ele use o cluster correto.
  config_context = "minikube"
}

provider "kubectl" {
  # Repetimos a configuração para o provedor kubectl.
  config_path    = "~/.kube/config"
}