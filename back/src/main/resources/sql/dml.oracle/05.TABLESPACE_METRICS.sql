SELECT tablespace_name,
       total_space,
       free_space,
       perc_used,
       percextend_used,
       max_size_mb,
       free_space_extend
FROM   (SELECT t1.tablespace_name,
               Round(used_space / 1024 / 1024)
               total_space,
               Round(Nvl(lib, 0) / 1024 / 1024)
               free_space,
               Round(100 * ( used_space - Nvl(lib, 0) ) / used_space, 1)
               perc_used,
               Round(100 * ( used_space - Nvl(lib, 0) ) / smax_bytes, 1)
                      percextend_used,
               Round(Nvl(smax_bytes, 0) / 1024 / 1024)
               max_size_mb,
               Round(Nvl(smax_bytes - ( used_space - Nvl(lib, 0) ), 0) / 1024 /
                     1024)
                      free_space_extend,
               nb_ext                                                    nb_ext
        FROM   (SELECT tablespace_name,
                       SUM(bytes) used_space
                FROM   dba_data_files i
                GROUP  BY tablespace_name) t1,
               (SELECT tablespace_name,
                       SUM(bytes)   lib,
                       Max(bytes)   max_nb,
                       Count(bytes) nb_ext
                FROM   dba_free_space
                GROUP  BY tablespace_name) t2,
               (SELECT tablespace_name,
                       SUM(max_bytes) smax_bytes
                FROM   (SELECT tablespace_name,
                               CASE
                                 WHEN autoextensible = 'YES' THEN
                                 Greatest(bytes, maxbytes)
                                 ELSE bytes
                               END max_bytes
                        FROM   dba_data_files i)
                GROUP  BY tablespace_name) t3
        WHERE  t1.tablespace_name = t2.tablespace_name(+)
               AND t1.tablespace_name = t3.tablespace_name(+))