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
Group User1   REGISTERED Process 1 IO_BLOCKED   (600        100        100        55         1          1         )
Group User2   REGISTERED Process 1 IO_BLOCKED   (317        10         100        62         1          91        )
Group User3   REGISTERED Process 1 COMPLETED    (45         450        45         40         1          0         )
Group System  REGISTERED Process 1 REGISTERED   (780        200        100        49         1          0         )
Group User1   REGISTERED Process 2 IO_BLOCKED   (545        100        100        47         1          1         )
Group User2   REGISTERED Process 2 IO_BLOCKED   (809        15         100        51         1          86        )
Group User3   REGISTERED Process 2 REGISTERED   (1121       2000       100        32         1          0         )
Group System  REGISTERED Process 0 IO_BLOCKED   (228        100        100        53         1          1         )
Group User1   REGISTERED Process 3 REGISTERED   (898        200        100        50         1          0         )
Group User2   REGISTERED Process 3 IO_BLOCKED   (203        20         100        61         1          81        )
Group User3   REGISTERED Process 3 IO_BLOCKED   (1221       100        100        45         1          1         )
Group System  REGISTERED Process 1 IO_BLOCKED   (780        200        200        49         2          1         )
Group User1   REGISTERED Process 0 IO_BLOCKED   (931        30         100        39         1          71        )
Group User2   REGISTERED Process 0 IO_BLOCKED   (600        10         100        55         1          91        )
Group User3   REGISTERED Process 0 IO_BLOCKED   (600        50         100        54         1          51        )
Group System  REGISTERED Process 0 IO_BLOCKED   (228        100        200        53         2          101       )
Group User1   REGISTERED Process 1 IO_BLOCKED   (600        100        200        55         2          101       )
Group User2   REGISTERED Process 1 IO_BLOCKED   (317        10         200        62         2          191       )
...
Group User1   REGISTERED Process 0 IO_BLOCKED   (931        30         800        39         8          771       )
Group User3   REGISTERED Process 2 REGISTERED   (1121       2000       1100       32         11         0         )
Group User1   REGISTERED Process 3 COMPLETED    (898        200        898        50         9          698       )
Group User3   REGISTERED Process 3 IO_BLOCKED   (1221       100        1100       45         11         1001      )
Group User1   REGISTERED Process 0 IO_BLOCKED   (931        30         900        39         9          871       )
Group User3   REGISTERED Process 2 COMPLETED    (1121       2000       1121       32         12         0         )
Group User1   REGISTERED Process 0 COMPLETED    (931        30         931        39         10         901       )
Group User3   REGISTERED Process 3 IO_BLOCKED   (1221       100        1200       45         12         1101      )
Group System  COMPLETED skips quantum
Group User3   REGISTERED Process 3 COMPLETED    (1221       100        1221       45         13         1121      )
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
Block mean50      
Block dev 10      
Runtime   10000   
Process  name   Cpu need    Block   Work    BlockT  Blocked        Status      Quantum received
Group System  
Process 0           228     100     228     53      128      times COMPLETED   3       
Process 1           780     200     780     49      580      times COMPLETED   8       
Group User1   
Process 0           931     30      931     39      901      times COMPLETED   10      
Process 1           600     100     600     55      500      times COMPLETED   6       
Process 2           545     100     545     47      445      times COMPLETED   6       
Process 3           898     200     898     50      698      times COMPLETED   9       
Group User2   
Process 0           600     10      600     55      590      times COMPLETED   6       
Process 1           317     10      317     62      307      times COMPLETED   4       
Process 2           809     15      809     51      794      times COMPLETED   9       
Process 3           203     20      203     61      183      times COMPLETED   3       
Group User3   
Process 0           600     50      600     54      550      times COMPLETED   6       
Process 1           45      450     45      40      0        times COMPLETED   1       
Process 2           1121    2000    1121    32      0        times COMPLETED   12      
Process 3           1221    100     1221    45      1121     times COMPLETED   13      

Summary:
Total           Cpu Need        Cpu Worked      Quantum got     Processes       Finished        
                8898            8898            96              14              14              
```

Summary gives totals for all processes in all groups 

#### Example with unfinished processes
Summary-Result.txt
```
Scheduling via: Fair Share algorithm
Quantum   100     
Mean      600     
Deviation 400     
Block mean50      
Block dev 10      
Runtime   10000   
Process  name   Cpu need    Block   Work    BlockT  Blocked        Status      Quantum received
Group System  
Process 0           1363    100     1363    45      1263     times COMPLETED   14      
Process 1           735     200     735     46      535      times COMPLETED   8       
Group User1   
Process 0           885     30      885     37      855      times COMPLETED   9       
Process 1           190     100     190     55      90       times COMPLETED   2       
Process 2           1166    100     900     47      801      times REGISTERED  9       
Process 3           580     200     580     43      380      times COMPLETED   6       
Group User2   
Process 0           510     10      510     29      500      times COMPLETED   6       
Process 1           61      10      61      59      51       times COMPLETED   1       
Process 2           1215    15      1000    45      986      times IO_BLOCKED  10      
Process 3           847     20      847     70      827      times COMPLETED   9       
Group User3   
Process 0           560     50      560     45      510      times COMPLETED   6       
Process 1           872     450     700     52      251      times REGISTERED  7       
Process 2           952     2000    700     66      0        times REGISTERED  7       
Process 3           600     100     600     46      500      times COMPLETED   6       

Summary:
Total           Cpu Need        Cpu Worked      Quantum got     Processes       Finished        
                10536           9631            100             14              10        
```


Summary-Processes.txt
```
GroupName     GroupState ProcessID ProcessState CPUneed     IOblock    CPUworked  BlockTime  QuantsGot  TimesBlocked  
``` 
```
Group User1   REGISTERED Process 1 IO_BLOCKED   (190        100        100        55         1          1         )
Group User2   REGISTERED Process 1 COMPLETED    (61         10         61         59         1          51        )
Group User3   REGISTERED Process 1 REGISTERED   (872        450        100        52         1          0         )
Group System  REGISTERED Process 1 REGISTERED   (735        200        100        46         1          0         )
Group User1   REGISTERED Process 2 IO_BLOCKED   (1166       100        100        47         1          1         )
Group User2   REGISTERED Process 2 IO_BLOCKED   (1215       15         100        45         1          86        )
Group User3   REGISTERED Process 2 REGISTERED   (952        2000       100        66         1          0         )
...
Group User1   REGISTERED Process 2 IO_BLOCKED   (1166       100        800        47         8          701       )
Group User2   REGISTERED Process 3 IO_BLOCKED   (847        20         800        70         8          781       )
Group User3   REGISTERED Process 3 COMPLETED    (600        100        600        46         6          500       )
Group User1   REGISTERED Process 0 IO_BLOCKED   (885        30         800        37         8          771       )
Group User2   REGISTERED Process 2 IO_BLOCKED   (1215       15         900        45         9          886       )
Group User3   REGISTERED Process 0 COMPLETED    (560        50         560        45         6          510       )
Group User1   REGISTERED Process 2 IO_BLOCKED   (1166       100        900        47         9          801       )
Group User2   REGISTERED Process 3 COMPLETED    (847        20         847        70         9          827       )
Group User3   REGISTERED Process 1 IO_BLOCKED   (872        450        700        52         7          251       )
Group User1   REGISTERED Process 0 COMPLETED    (885        30         885        37         9          855       )
Group User2   REGISTERED Process 2 IO_BLOCKED   (1215       15         1000       45         10         986       )
Group User3   REGISTERED Process 2 REGISTERED   (952        2000       700        66         7          0         )
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

```ProcessSimulation``` is a leave version of ```ActionOnQuantum``` so for next available it returns itself and works with time quantum

Next available for each step is chosen if it's ```ProcessState``` is NOT ``COMPLETED`` or ``IO_BLOCKED`` 