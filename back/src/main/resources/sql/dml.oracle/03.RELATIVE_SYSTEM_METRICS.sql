SELECT metric_name,
       value,
       metric_unit
FROM   v$sysmetric
WHERE  group_id = 2 AND metric_unit LIKE '%!%%' ESCAPE '!'