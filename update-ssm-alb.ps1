# Script para atualizar o ARN do Listener do ALB no SSM Parameter Store
param (
    [string]$Region = "us-east-1"
)

Write-Host "Iniciando atualizaçao do SSM com ARN do ALB..."

# 1. Encontrar o Load Balancer criado pelo Ingress
try {
    # Busca o ARN do LB que contém 'techchallenge' no nome do DNS
    $lbArn = aws elbv2 describe-load-balancers --region $Region --query "LoadBalancers[?contains(DNSName, 'techchallenge')].LoadBalancerArn" --output text --no-cli-pager
}
catch {
    Write-Error "Falha ao executar AWS CLI. Verifique credenciais."
    exit 1
}

if (-not $lbArn -or $lbArn -eq "None" -or $lbArn -eq "") {
    Write-Warning "Nenhum Load Balancer com 'techchallenge' no nome encontrado. O Ingress ja terminou de criar?"
    exit 1
}

Write-Host "Load Balancer encontrado: $lbArn"

# 2. Encontrar o Listener na porta 80 (HTTP)
$listenerArn = aws elbv2 describe-listeners --load-balancer-arn $lbArn --region $Region --query "Listeners[?Port==\`80\`].ListenerArn" --output text --no-cli-pager

if (-not $listenerArn -or $listenerArn -eq "None" -or $listenerArn -eq "") {
    Write-Error "Listener na porta 80 nao encontrado para este Load Balancer."
    exit 1
}

Write-Host "Listener encontrado: $listenerArn"

# 3. Salvar no SSM Parameter Store
$paramName = "/techchallenge/alb_listener_arn"
Write-Host "Salvando em $paramName..."

aws ssm put-parameter --name $paramName --value $listenerArn --type String --overwrite --region $Region --no-cli-pager

Write-Host "Sucesso! SSM atualizado com valor: $listenerArn"
