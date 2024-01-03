output "te_resource_group_name" {
  value = azurerm_resource_group.main.name
}

output "te_public_ip_address" {
  value = azurerm_linux_virtual_machine.main.public_ip_address
}

output "te_tls_private_key" {
  value     = tls_private_key.main.private_key_pem
  sensitive = true
}

output "jenkins_resource_group_name" {
  value = azurerm_resource_group.main.name
}

output "jenkins_public_ip_address" {
  value = azurerm_linux_virtual_machine.jenkins.public_ip_address
}

output "jenkins_tls_private_key" {
  value     = tls_private_key.jenkins.private_key_pem
  sensitive = true
}