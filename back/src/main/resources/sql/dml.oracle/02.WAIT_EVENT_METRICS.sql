SELECT n.wait_class                                           wait_class,
       n.name                                                 wait_name,
       m.wait_count                                           cnt,
       Round(10 * m.time_waited / Nullif(m.wait_count, 0), 3) avgms
FROM   v$eventmetric m,
       v$event_name n
WHERE  m.event_id = n.event_id
       AND n.name = 'enq: HW - contention';