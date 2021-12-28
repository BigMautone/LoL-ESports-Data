CREATE function rateo(u int, m int, a int)
returns float(2)
BEGIN
	DECLARE rateo float(2);
    SET rateo = (u+a)/m;
    RETURN rateo;
END