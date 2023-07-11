$resourceGroupName="myResourceGroup"
$location = "East US"
$ehubName="myEventHub"
$namespaceName="myNamespace2023"
$functionAppName = "AzureFunctionTriggerApp2023"
$runtime = "powershell"
$storageAccountName = "testdatastorage2023"
$accountName = "mydb2023"
$databaseName = "sql-db-2023"
New-AzResourceGroup –Name $resourceGroupName –Location $location
New-AzEventHubNamespace -ResourceGroupName $resourceGroupName -Name $namespaceName -Location $location
New-AzEventHub -ResourceGroupName $resourceGroupName -NamespaceName $namespaceName -EventHubName $ehubName
New-AzStorageAccount -ResourceGroupName $resourceGroupName -Name $storageAccountName -Location $location -SkuName Standard_LRS
New-AzAppServicePlan -ResourceGroupName $resourceGroupName -Name "$functionAppName-plan" -Location $location -Tier Free
New-AzFunctionApp -Name $functionAppName -ResourceGroupName $resourceGroupName -StorageAccount $storageAccountName -Runtime PowerShell -FunctionsVersion 4 -Location $location
New-AzCosmosDBAccount -ResourceGroupName $resourceGroupName -Name $accountName -Location $location -DefaultConsistencyLevel "Session"
$database = New-AzCosmosDBSqlDatabase -AccountName $accountName -ResourceGroupName $resourceGroupName -Name $databaseName
$account = Get-AzCosmosDBAccount -ResourceGroupName $resourceGroupName -Name $accountName
$connectionString = $account.DocumentEndpoint + ";AccountKey=" + (Get-AzCosmosDBAccountKey -ResourceGroupName $resourceGroupName -Name $accountName).PrimaryMasterKey
Write-Output "Cosmos DB Connection String: $connectionString"