#################################################
#																											#
#		Powershell scripts to create resources in Azure					#
#																											#
#################################################


# Define variables

$resourceGroupName="az-nashtech-resource-group"
$randomIdentifier = Get-Random
$location = "East US"
$ehubName="EventHub"
$namespaceName="az-nashtech-eventhub"
$functionAppName = "CarFactoryAzFunction"
$storageAccountName = "nashtechstorageaccount"
$accountName = "az-nashtech-reactive-database"
$databaseName = "az-nashtech-db"
$functionPlan = "az-functionapp-plan"
$skuPlan = "B1"
$apiKind = "SQL"
$clusterName = "az-nashtech-cluster"

write-host("Starting process to create resources for the application....")

# Create new resource group
write-host("Creating resource group {0}" -f $resourceGroupName.value)
New-AzResourceGroup –Name $resourceGroupName –Location $location
write-host("Resource group created")

# Create EventHub
write-host("Creating eventhub namespace {0}" -f $namespaceName.value)
$isEventHubRegistered = Get-AzResourceProvider -ListAvailable Where-Object -Property ProviderNamespace -Like -Value "Microsoft.EventHub"
if($isEventHubRegistered.RegistrationState -eq "Registered") {
    write-host("Event hub provider already registered")
}
else {
    write-host("Registering EventHub provider space")
    Register-AzResourceProvider -ProviderNamespace "Microsoft.EventHub"
}
New-AzEventHubNamespace -ResourceGroupName $resourceGroupName -Name $namespaceName -Location $location
write-Host("Event hub name space created successfully")
write-host("Creating eventhub topic {0}" -f $ehubName.value)
New-AzEventHub -ResourceGroupName $resourceGroupName -NamespaceName $namespaceName  -PartitionCount 4 -EventHubName $ehubName
write-Host("Event hub topic created successfully")


# Create storage account
write-host("Creating storage account {0}" -f $storageAccountName.value)
$isStorageRegistered = Get-AzResourceProvider -ListAvailable Where-Object -Property ProviderNamespace -Like -Value "Microsoft.Storage"
if($isStorageRegistered.RegistrationState -eq "Registered") {
    write-host("Storage already registered")
}
else {
    write-host("Registering storage provider space")
    Register-AzResourceProvider -ProviderNamespace "Microsoft.Storage"
}
New-AzStorageAccount -ResourceGroupName $resourceGroupName -Name $storageAccountName -Location $location -SkuName Standard_LRS
write-host("Storage created successfully")

# Create Document DB
$isDocDbRegistered = Get-AzResourceProvider -ListAvailable Where-Object -Property ProviderNamespace -Like -Value "Microsoft.DocumentDB"
if($isDocDbRegistered.RegistrationState -eq "Registered") {
    write-host("DocumentDB provider already registered")
}
else {
    write-host("Registering DocumentDB provider space")
    Register-AzResourceProvider -ProviderNamespace "Microsoft.DocumentDB"
}
New-AzCosmosDBAccount -ResourceGroupName $resourceGroupName -Name $accountName -Location $location -ApiKind $apiKind  -DefaultConsistencyLevel "Session"
$database = New-AzCosmosDBSqlDatabase -AccountName $accountName -ResourceGroupName $resourceGroupName -Name $databaseName
$account = Get-AzCosmosDBAccount -ResourceGroupName $resourceGroupName -Name $accountName
$connectionString = $account.DocumentEndpoint + ";AccountKey=" + (Get-AzCosmosDBAccountKey -ResourceGroupName $resourceGroupName -Name $accountName).PrimaryMasterKey
Write-Output "CNoSql DB Connection String: $connectionString"
$endpoint = (Get-AzCosmosDBAccount -Name $accountName -ResourceGroupName $resourceGroupName).DocumentEndpoint
Write-Host $endpoint
$key = (Get-AzCosmosDBAccountKey -Name $accountName -ResourceGroupName $resourceGroupName).PrimaryMasterKey
Write-Host $key


# Create azure function
write-host("Creating storage account {0}" -f $functionPlan.value)
$isWebProviderRegistered = Get-AzResourceProvider -ListAvailable Where-Object -Property ProviderNamespace -Like -Value "Microsoft.Web"
if($isWebProviderRegistered.RegistrationState -eq "Registered") {
    write-host("Web provider already registered")
}
else {
    write-host("Registering Web provider space")
    Register-AzResourceProvider -ProviderNamespace "Microsoft.Web"
}
New-AzFunctionAppPlan -Name $functionPlan -ResourceGroupName $resourceGroupName -Location $location -Sku $skuPlan -WorkerType Linux
write-Host("Function app plan created successfully")
write-host("Creating azure function {0}" -f $functionAppName.value)
New-AzFunctionApp -Name $functionAppName -StorageAccountName $storageAccountName -PlanName $functionPlan -ResourceGroupName $resourceGroupName -Runtime Java -RuntimeVersion 17 -OSType Linux -FunctionsVersion 4
write-Host("Function app created successfully")
write-Host("Configuring function to connect with cosmos")
Update-AzFunctionAppSetting -Name $functionAppName -ResourceGroupName $resourceGroupName -AppSetting @{CosmosDB_Endpoint = $endpoint; CosmosDB_Key = $key}
write-Host("Enabling app insights on function")
New-AzApplicationInsights -ResourceGroupName $resourceGroupName -Name $functionAppName -location $location


# Create AKS cluster
write-host("Creating kubernetes cluster {0}" -f $clusterName.value)
$isKubeRegistered = Get-AzResourceProvider -ListAvailable Where-Object -Property ProviderNamespace -Like -Value "Microsoft.Kubernetes"
if($isKubeRegistered.RegistrationState -eq "Registered") {
    write-host("Kubernetes provider already registered")
}
else {
    write-host("Kubernetes DocumentDB provider space")
    Register-AzResourceProvider -ProviderNamespace "Microsoft.Kubernetes"
}
New-AzAksCluster -ResourceGroupName $resourceGroupName -Name $clusterName -NodeCount 1 -GenerateSshKey
write-host("Kubernetes cluster creaed successfully")

write-host("Process completed successfully")