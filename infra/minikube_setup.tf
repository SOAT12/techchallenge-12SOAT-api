resource "null_resource" "minikube_starter" {
  triggers = {
    always_run = timestamp()
  }

  provisioner "local-exec" {
    command = "minikube start --cpus 4 --memory 4192"
  }
}