SELECT metric_name,
       value,
       metric_unit
FROM   v$sysmetric
WHERE  group_id = 2 AND metric_name in (
    'Physical Reads Per Sec',
    'Physical Writes Per Sec',
    'Logons Per Sec',
    'Open Cursors Per Sec',
    'User Commits Per Sec',
    'User Rollbacks Per Sec',
    'Redo Writes Per Sec',
    'Physical Read Total IO Requests Per Sec',
    'Executions Per Sec'
)