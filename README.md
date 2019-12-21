# Scheduler
## Scheduler simulator with Fair Share algorithm

This is a modification of <a href="http://www.ontko.com/moss/#sched">Modern Operating Systems Simulators scheduler</a>.


## Difference from original
Unlike original scheduler this version uses XML files to configure the system, default configuration file can be found <a href="https://github.com/SergySanJj/Scheduler/blob/master/Scheduler/res/defaultConfig.xml">here (```Scheduler/res/defaultConfig.xml```)</a> 

To change config you can either modify default config or create your own by using next <a href="https://github.com/SergySanJj/Scheduler/blob/master/Scheduler/res/processes.xsd">schema (```Scheduler/Scheduler/res/processes.xsd```)</a> and then passing it location as command line argument when starting program.

## How to run
Set your working directory to ```..\Scheduler\Scheduler```

Set your main class to ```com.scheduler.scheduler.Scheduling```

[optional] Set your program arguments as path to your config file, if left empty - will use ```res/defaultConfig.xml```

## System
Process structure is described by .xsd scheme. 

On the top level we have root node ```simulation```, this node have children ```groups```


## Input
config.xml
```
<processes>
    <meandev>600</meandev>
    <standdev>400</standdev>
    <blockMean>50</blockMean>
    <blockDeviation>10</blockDeviation>
    <runtime>10000</runtime>
    <quantum>100</quantum>

    <group>
        <name>System</name>
        <process>100</process>
        <process>200</process>
    </group>
    <group>
        <name>User1</name>
        <process>30</process>
        <process>100</process>
        <process>100</process>
        <process>200</process>
    </group>
</processes>
```

```<processes>``` - root that corresponds to simulation root

```meandev``` - mean deviation for each process total needed cpu run time

```standdev``` - standard deviation for each process total needed cpu run time

```blockMean```  - mean deviation for each process IO block time

```blockDeviation```  - standard deviation for each process IO block time

```runtime``` - total cpu time for simulation

```quantum``` - cpu time quantum could be sent to process

```<group>``` - group block

```<name>string</name>```  - string is a group name

```<process>integer</process>``` - integer is cpu time that process will run without blocking for IO



## Runtime 
When program of ```Scheduling``` is run it parses config file into object of <a href="https://github.com/SergySanJj/Scheduler/blob/master/Scheduler/src/main/java/com/scheduler/scheduler/simulation/Simulation.java">``` com.scheduler.scheduler.simulation.Simulation```</a> class

Then it runs chosen ```SchedulerAlgorithm``` interface implementation (in showed case this implementation is <a href="https://github.com/SergySanJj/Scheduler/blob/master/Scheduler/src/main/java/com/scheduler/scheduler/FairShare.java">```FairShare``` (```src/main/java/com/scheduler/scheduler/FairShare.java```)</a>) implementation also writes simulation log to ```PrintStream``` that were passed.

Finaly program writes result of simulation to file.

## Output

Can be found at <a href="https://github.com/SergySanJj/Scheduler/tree/master/Scheduler/res">```Scheduler/res/```</a> in files:

<a href="https://github.com/SergySanJj/Scheduler/blob/master/Scheduler/res/Summary-Processes.txt">```Summary-Processes.txt```</a> - processing log on each scheduler tick

<a href="https://github.com/SergySanJj/Scheduler/blob/master/Scheduler/res/Summary-Results.txt">```Summary-Results.txt```</a> - final state of simulation with summary

#### Process output example
Summary-Processes.txt
```
GroupName     GroupState ProcessID ProcessState CPUneed     IOblock    CPUworked  BlockTime  QuantsGot  TimesBlocked  
``` 
```
Group User1   REGISTERED Process 1 IO_BLOCKED   (1302       100        100        117        1          1         )
Group User2   REGISTERED Process 1 IO_BLOCKED   (500        10         100        111        1          10        )
Group User3   REGISTERED Process 1 REGISTERED   (1013       450        100        99         1          0         )
Group System  REGISTERED Process 1 REGISTERED   (104        200        100        123        1          0         )
Group User1   REGISTERED Process 2 IO_BLOCKED   (158        100        100        97         1          1         )
Group User2   REGISTERED Process 2 IO_BLOCKED   (109        15         100        104        1          6         )
Group User3   REGISTERED Process 2 REGISTERED   (1158       2000       100        90         1          0         )
Group System  REGISTERED Process 0 IO_BLOCKED   (633        100        100        101        1          1         )
...
Group User3   REGISTERED Process 1 COMPLETED    (1013       450        1013       99         11         2         )
Group User1   REGISTERED Process 1 IO_BLOCKED   (1302       100        1300       117        13         13        )
Group User3   REGISTERED Process 2 REGISTERED   (1158       2000       1100       90         11         0         )
Group User3   REGISTERED Process 2 COMPLETED    (1158       2000       1158       90         12         0         )
Group User1   REGISTERED Process 1 COMPLETED    (1302       100        1302       117        14         13        )
Group System  COMPLETED skips quantum
Group System  COMPLETED skips quantum
Group System  COMPLETED skips quantum
Group System  COMPLETED skips quantum
```

```GroupName``` - name of group that got quantum

```GroupState``` - current state of group can be (```PENDING, REGISTERED, IO_BLOCKED, COMPLETED```)

```ProcessID``` - id of process inside group

```ProcessState``` - current state of process can be (```PENDING, REGISTERED, IO_BLOCKED, COMPLETED```)

```CPUneed``` - total amount of cpu time needed for process to be completed

```IOblock``` - cpu time that process will run without blocking for IO 

```CPUworked``` - total amount of cpu time that process have worked

```BlockTime``` - time for which process is blocked when it is waiting for IO

```QuantsGot``` - total number of quantums that were received by process

```TimesBlocked``` - total number of blocks done by process


#### Result output example
Summary-Result.txt
```
Scheduling via: Fair Share algorithm
Quantum   100     
Mean      600     
Deviation 400     
Block mean100     
Block dev 20      
Runtime   10000   
Process  name   Cpu need    Block   Work    BlockT  Blocked        Status      Quantum received
Group System  
Process 0           697     100     697     153     6        times COMPLETED   7       
Process 1           522     200     522     117     2        times COMPLETED   6       
Group User1   
Process 0           1067    30      1067    104     35       times COMPLETED   11      
Process 1           983     100     983     75      9        times COMPLETED   10      
Process 2           765     100     765     79      7        times COMPLETED   8       
Process 3           982     200     982     97      4        times COMPLETED   10      
Group User2   
Process 0           712     10      712     96      71       times COMPLETED   8       
Process 1           743     10      743     87      74       times COMPLETED   8       
Process 2           1010    15      1010    112     67       times COMPLETED   11      
Process 3           600     20      600     106     29       times COMPLETED   6       
Group User3   
Process 0           13      50      13      84      0        times COMPLETED   1       
Process 1           133     450     133     106     0        times COMPLETED   2       
Process 2           269     2000    269     126     0        times COMPLETED   3       
Process 3           448     100     448     82      4        times COMPLETED   5       

Summary:
Total           Cpu Need        Cpu Worked      Quantum got     Processes       Finished        
                8944            8944            96              14              14   
            
```

Summary gives totals for all processes in all groups 

#### Example with unfinished processes
Summary-Result.txt
```
Scheduling via: Fair Share algorithm
Quantum   100     
Mean      1000    
Deviation 400     
Block mean100     
Block dev 20      
Runtime   10000   
Process  name   Cpu need    Block   Work    BlockT  Blocked        Status      Quantum received
Group System  
Process 0           979     100     979     77      9        times COMPLETED   10      
Process 1           839     200     839     116     4        times COMPLETED   9       
Group User1   
Process 0           914     30      600     82      20       times REGISTERED  6       
Process 1           1007    100     700     91      7        times REGISTERED  7       
Process 2           623     100     623     115     6        times COMPLETED   7       
Process 3           692     200     692     106     3        times COMPLETED   7       
Group User2   
Process 0           499     10      499     79      49       times COMPLETED   5       
Process 1           1456    10      800     94      80       times IO_BLOCKED  8       
Process 2           1099    15      700     109     46       times REGISTERED  7       
Process 3           1047    20      700     110     35       times REGISTERED  7       
Group User3   
Process 0           723     50      600     92      12       times REGISTERED  6       
Process 1           790     450     700     88      1        times REGISTERED  7       
Process 2           969     2000    700     69      0        times REGISTERED  7       
Process 3           1015    100     700     109     7        times IO_BLOCKED  7       

Summary:
Total           Cpu Need        Cpu Worked      Quantum got     Processes       Finished        
                12652           9832            100             14              5                     
```



# Algorithm

For this case was used Fair Share algorithm with Round-robin on each group layer. Currently system shows work for 2 layers (Group layer and Process layer) but it can be extended by using more implementations of ```interface ActionOnQuantum```. 

Exact behaviour of each layer on getting a time quantum is defined by realisation of method ``` String receiveQuantum(int quantum, int currentTime) ```.

## Pipeline

Algorithm starts via run method from ```FairShare``` class

While ```FairShare``` has cpu time it produces quantums of time and sends them to ```Simulation``` via ```RoundRobinMultiLayer.run(..);```

Round-robin algorithm calls ```receiveQuantum``` for next available ```ActionOnQuantum``` element (our case ```Simulation```)

```Simulation``` passes quantum to next available process group via ```receiveQuantum```

```ProcessGroup``` passes quantum to next available process via ```receiveQuantum```

```ProcessSimulation``` is a leaf version of ```ActionOnQuantum``` so for next available it returns itself and works with time quantum

Next available for each step is chosen if it's ```ProcessState``` is NOT ``COMPLETED`` or ``IO_BLOCKED`` 
