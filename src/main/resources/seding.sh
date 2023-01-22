#!/bin/sh
#
# This file was last modified at 2023.01.22 18:04 by Victor N. Skurikhin.
# This is free and unencumbered software released into the public domain.
# For more information, please refer to <http://unlicense.org>
# seding.sh
# $Id$
#

list_files="
./src/main/java/su/svn/daybook/domain/dao/KeyValueDao.java
./src/main/java/su/svn/daybook/domain/model/KeyValueTable.java
./src/main/java/su/svn/daybook/models/domain/KeyValue.java
./src/main/java/su/svn/daybook/resources/KeyValueResource.java
./src/main/java/su/svn/daybook/services/cache/KeyValueCacheProvider.java
./src/main/java/su/svn/daybook/services/domain/KeyValueDataService.java
./src/main/java/su/svn/daybook/services/mappers/KeyValueMapper.java
./src/main/java/su/svn/daybook/services/models/KeyValueService.java
./src/test/java/su/svn/daybook/domain/model/KeyValueTableTest.java
./src/test/java/su/svn/daybook/models/domain/KeyValueTest.java
./src/test/java/su/svn/daybook/services/models/KeyValueServiceTest.java
./src/test/java/su/svn/daybook/KeyValueDataBaseIT.java
"
pattern_file_name='s/KeyValue/Dictionary/'
list_pattern='
s/3377791800667728148/@serialVersionUID@/g;
s/3421670798382710094/@serialVersionUID@/g;
s/d94d93d9-d44c-403c-97b1-d071b6974d80/@uuid@/g;
s/UUID/@IdType@/g;
s/BigInteger/@KType@/g;
s/keyValue/@name@/g;
s/KeyValue/@Name@/g
s/key_value/@table@/g;
s/Key/@Key@/g;
s/DICTIONARY/@SCHEMA@/g;
s/KEY_VALUE/@TABLE@/g;
s/JsonObject/@VType@/g;
s/Value/@Value@/g;
s/key/@key@/g;
s/value/@value@/g;
s/dictionary/@schema@/g;
s/dictionary/@schema@/g;
s/row.getBigDecimal("@key@").to@KType@()/row.getKey("@key@").to@KType@()/g;
s/findBy@Key@SQL/findByKeySQL/g;
s/findBy@Value@SQL/findBy@alueSQL/g
s/Cache@Key@/CacheKey/g;
s/invalidateBy@Key@/invalidateByKey/g;
s/invalidateCacheBy@Key@/invalidateCacheByKey/g'
for file in $list_files
do
  directory=${file%/*}
  echo $directory
  base_file_name=${file##*/}
  echo $base_file_name
  new_file_name=$(echo "${base_file_name}" | gsed -e $pattern_file_name)
  gsed -e "${list_pattern}" $directory/$base_file_name > ./src/tmp-templates/$new_file_name
done