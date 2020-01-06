SELECT n.wait_class,
       Round(m.time_waited / m.intsize_csec, 3) VALUE
FROM   v$waitclassmetric m,
       v$system_wait_class n
WHERE  m.wait_class_id = n.wait_class_id
       AND n.wait_class != 'Idle'
UNION
SELECT 'CPU',
       Round(value / 100, 3) VALUE
FROM   v$sysmetric
WHERE  metric_name = 'CPU Usage Per Sec'
       AND group_id = 2
UNION
SELECT 'CPU_OS',
       ( Round(( prcnt.busy * parameter.cpu_count ) / 100, 3) - aas.cpu ) VALUE
FROM   (SELECT value busy
        FROM   v$sysmetric
        WHERE  metric_name = 'Host CPU Utilization (%)'
               AND group_id = 2) prcnt,
       (SELECT value cpu_count
        FROM   v$parameter
        WHERE  name = 'cpu_count') parameter,
       (SELECT 'CPU',
               Round(value / 100, 3) cpu
        FROM   v$sysmetric
        WHERE  metric_name = 'CPU Usage Per Sec'
               AND group_id = 2) aas;