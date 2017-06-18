#powershell timeeeeeeeeee
#i need a script
#i can do it in java
#howeeeever
#my inside admin screams
#i've been a w$ admin for so long
#still vb sucks dicks

param($File, $classFile)

$csv = import-csv $File

#now, let's replace
#but, first, import the whole text file
$count = 0
Foreach($row in $csv){

    $nameOfYourClass = $row.ClassName
    $name = $row.Name
    $children = $row.Children
    $funcNo = $row.FuncNo

    $wholeText= Get-Content $classFile
    $package = "nonTerminal"
    if ($children -eq 0) {$package = "terminals"}

    $javaFileName = "..\GPBot\src\"+$package+'\'+$nameOfYourClass + ".java"

    #let's think this works
    $wholeText = $wholeText.replace('$package', $package) 
    $wholeText = $wholeText.replace('$nameOfYourClass', $nameOfYourClass) 
    $wholeText = $wholeText.replace('$name', $name) 
    $wholeText = $wholeText.replace('$children', $children) 
    $wholeText = $wholeText.replace('$unitType', $unitType) 
    
    'gp.fs.'+$funcNo+'.func.'+$count+' = '+$package+'.' + $nameOfYourClass >> '..\GPBot\allmynodes.params'
    'gp.fs.'+$funcNo+'.func.'+$count+'.nc = nc'+$children >> '..\GPBot\allmynodes.params'
    $count = $count + 1
    }
