create or replace function GET_ALLCODE_BY_BIN(indexNumber NUMBER,lengthNumber number)
return Varchar2 is
  FunctionResult Varchar2(32);--返回值
begin
  if lengthNumber = 16 then
    if indexNumber=0 then
      FunctionResult :='0000000000000000';
    else
      FunctionResult :=substr('0000000000000000', 0,16-length(indexNumber))||indexNumber;
    end if;
  elsif lengthNumber = 32 then
    if indexNumber=0 then
      FunctionResult :='00000000000000000000000000000000';
    else
      FunctionResult :=substr('00000000000000000000000000000000', 0,32-length(indexNumber))||indexNumber;
    end if;
  end if;
  return(FunctionResult);
end GET_ALLCODE_BY_BIN;

#######################################################################################
CREATE OR REPLACE FUNCTION num_to_bin (p_num NUMBER) RETURN VARCHAR2
IS
   r_binstr   VARCHAR2 (32767);
   l_num      NUMBER           := p_num;
BEGIN
  if l_num=0 then
    r_binstr:='0';
  else
   WHILE l_num != 0 LOOP
      r_binstr := TO_CHAR (MOD (l_num, 2)) || r_binstr;
      l_num := TRUNC (l_num / 2);
   END LOOP;
  end if;
   RETURN r_binstr;
END num_to_bin;

#######################################################################################

create or replace function GET_BINCODE_BY_INDEX( indexNumber NUMBER ,lengthNumber number)
return varchar2 is
  FunctionResult varchar2(32);--返回值
begin
  if lengthNumber = 16 and indexNumber<=16 then
  FunctionResult := regexp_replace('0000000000000000','(.)','1',indexNumber,1);
  elsif lengthNumber = 32 and indexNumber<=32 then 
  FunctionResult := regexp_replace('00000000000000000000000000000000','(.)','1',indexNumber,1); 
 end if; 
 return(FunctionResult);
end GET_BINCODE_BY_INDEX;

#######################################################################################################
CREATE OR REPLACE type strsplit_type
AS
  TABLE OF VARCHAR2(50);
  
#######################################################################################################
CREATE OR REPLACE FUNCTION strsplit(
    p_value VARCHAR2,
    p_split VARCHAR2 := ',')
  RETURN strsplit_type pipelined
IS
  v_idx       INTEGER;
  v_str       VARCHAR2(500);
  v_strs_last VARCHAR2(4000) := p_value;
BEGIN
  IF p_value IS NULL OR LENGTH(trim(p_value))<1 THEN
    RETURN;
  END IF;
IF p_split IS NULL OR LENGTH(trim(p_split))<1 THEN
  pipe row(p_value);
  RETURN;
END IF;
LOOP
  v_idx := instr(v_strs_last, p_split);
  EXIT
WHEN v_idx     = 0;
  v_str       := SUBSTR(v_strs_last, 1, v_idx - 1);
  v_strs_last := SUBSTR(v_strs_last, v_idx    + 1);
  pipe row(v_str);
END LOOP;
pipe row(v_strs_last);
RETURN;
END strsplit;






