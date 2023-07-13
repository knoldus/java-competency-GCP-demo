$resourceGroupName="myResourceGrp"
$randomIdentifier = Get-Random
$location = "East US"
$ehubName="myEventHub"
$namespaceName="myNamespace2023125"
$functionAppName = "AzureFunction202314"
$storageAccountName = "storageaccount232009"
$accountName = "reactive-database-twenty-$randomIdentifier"
$databaseName = "sql-db-twenty-six"
$functionPlan = "functionAppName-plans"
$skuPlan = "B1"
$apiKind = "MongoDB"
New-AzResourceGroup –Name $resourceGroupName –Location $location
New-AzEventHubNamespace -ResourceGroupName $resourceGroupName -Name $namespaceName -Location $location
New-AzEventHub -ResourceGroupName $resourceGroupName -NamespaceName $namespaceName  -PartitionCount 4 -EventHubName $ehubName
New-AzStorageAccount -ResourceGroupName $resourceGroupName -Name $storageAccountName -Location $location -SkuName Standard_LRS
New-AzFunctionAppPlan -Name $functionPlan -ResourceGroupName $resourceGroupName -Location $location -Sku $skuPlan -WorkerType Linux
New-AzFunctionApp -Name $functionAppName -StorageAccountName $storageAccountName -PlanName $functionPlan -ResourceGroupName $resourceGroupName -Runtime Java -RuntimeVersion 17 -OSType Linux -FunctionsVersion 4
New-AzCosmosDBAccount -ResourceGroupName $resourceGroupName -Name $accountName -Location $location -ApiKind $apiKind  -DefaultConsistencyLevel "Session"
$database = New-AzCosmosDBMongoDBDatabase -AccountName $accountName -ResourceGroupName $resourceGroupName -Name $databaseName
$account = Get-AzCosmosDBAccount -ResourceGroupName $resourceGroupName -Name $accountName
$connectionString = $account.DocumentEndpoint + ";AccountKey=" + (Get-AzCosmosDBAccountKey -ResourceGroupName $resourceGroupName -Name $accountName).PrimaryMasterKey
Write-Output "Cosmos DB Connection String: $connectionString"
$endpoint = (Get-AzCosmosDBAccount -Name $accountName -ResourceGroupName $resourceGroupName).DocumentEndpoint
Write-Host $endpoint
$key = (Get-AzCosmosDBAccountKey -Name $accountName -ResourceGroupName $resourceGroupName).PrimaryMasterKey
Write-Host $key
Update-AzFunctionAppSetting -Name $functionAppName -ResourceGroupName $resourceGroupName -AppSetting @{CosmosDB_Endpoint = $endpoint; CosmosDB_Key = $key}
New-AzApplicationInsights -ResourceGroupName $resourceGroupName -Name $functionAppName -location $location