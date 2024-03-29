-- op1:Aggiornamento delle statistiche dei giocatori (In questo caso i dati di aggiornamento sono preimpostati. Nell'applicazione è possibile selzionarli)
UPDATE statistiche SET Uccisioni = (Uccisioni + 12), Morti = (Morti + 5), Assist = (Assist + 10) WHERE Giocatore = "Faker";

-- op2: Dato il rateo di ogni giocatore, elenca Nickname,nome,cognome,età e squadra di un giocatore che ha meno di 20 anni con rateo più alto che gioca in una squadra da meno di 1 anno --
SELECT Id,Nome,Cognome,Squadra, (year(CURDATE()) - year(Data_di_nascita)) as età, rateo(Uccisioni,Morti,Assist) as rateo
FROM giocatore,contratto as c,statistiche as s
WHERE (Id=s.Giocatore) AND (Id=c.Giocatore) AND (rateo(Uccisioni,Morti,Assist) > 2.00)
AND (year(CURDATE()) - year(Data_di_nascita) < 20)
AND (year(CURDATE()) - year(Data_inizio) <= 1);


-- op3: Elenca tutte le squadre che hanno vinto le diverse edizioni dei mondiali(Season World Championship)
SELECT DISTINCT Squadra_vincitrice as Squadra
FROM edizioni_torneo
WHERE Torneo = "Season World Championship";

-- op4: Trova tutti i giocatori della stessa nazionalità della squadra in cui giocano attualmente
Select Id,Giocatore.nazionalità as Nazionalità
from giocatore,contratto,squadra
where (giocatore.nazionalità = squadra.Nazionalità)
and (Id = contratto.giocatore and scaduto = 0);

-- op5: Trova tutte le informazioni dei membri dello staff di una squadra che guadagnano più di uno o più proprietari di un’altra squadra 
-- oppure più di uno o più giocatori della stessa squadra
SELECT *
FROM staff as s1
WHERE (Stipendio > any(SELECT Stipendio FROM staff as s2 WHERE Proprietario = 1 and s1.Squadra <> s2.Squadra)
OR (Stipendio > any(SELECT Stipendio FROM contratto as c WHERE c.Squadra = s1.Squadra AND c.Scaduto=0)))
AND Proprietario = 0;


-- op6: Dati i giocatori che hanno vinto almeno 2 tornei giocando nella stessa squadra, elenca Nickname del giocatore, squadra e numero trofei vinti
SELECT vinto.Giocatore,Squadra_vincitrice, count(*) as trofei
FROM vinto,edizioni_torneo
WHERE (vinto.Torneo = edizioni_torneo.Torneo and vinto.Anno = edizioni_torneo.Anno)
 and(giocatore = any (select Giocatore from vinto 
							group by giocatore
							having count(*) >= 2))
GROUP BY giocatore;




-- op7: Trova gli allenatori che allenano una squadra che ha vinto almeno un mondiale oppure almeno 2 tornei nazionali
SELECT Id as Allenatore,count(*) as num_trofei,torneo.tipo
FROM allenatore,edizioni_torneo,torneo
WHERE (Squadra=Squadra_vincitrice) AND (torneo.Nome = Torneo)
AND (Squadra IN (SELECT Squadra_vincitrice 
				FROM edizioni_torneo,Torneo
				WHERE Nome=Torneo
				AND (Tipo='Mondiale' or Tipo='Nazionale')
				GROUP BY Squadra_vincitrice
				HAVING (count(tipo='Mondiale') >= 1 or count(tipo='Nazionale') >=2)))
GROUP by Id
ORDER BY num_trofei desc;



-- op8: Elenca le squadre che hanno giocato partite da 5 turni ribaltando la serie da 0-2 a 3-2 o viceversa
SELECT Nome
FROM Squadra
WHERE EXISTS (select * 
			from partita 
			where turno=5 
			and ((Risultato="3-2" and Nome = Squadra1) or (Risultato="2-3" and Nome = Squadra2))
			and Data_partita in (select Data_partita
								from partita
								where turno=2
								and (Risultato = "0-2" or Risultato = "2-0")));


-- OP9: Dato un giocatore, mostra il numero di partite vinte
SELECT Giocatore,Squadra, count(*) as Partite_vinte
FROM contratto as c, partita as p
WHERE Scaduto = 0 AND ((c.Squadra = p.Squadra1) OR (c.Squadra = p.Squadra2))
AND (Risultato LIKE "3%" OR Risultato LIKE "%3")
AND giocatore = "Faker"
group by Giocatore;

-- op10: Trova le 2 squadre che si sono affrontate più volte
SELECT IF(Squadra1 < Squadra2, concat(Squadra1," - ", Squadra2),concat(Squadra2," - ", Squadra1)) as Squadre,count(*) as num_partite
from partita as p1
GROUP BY Squadre 
ORDER BY num_partite DESC
LIMIT 1;

-- op11: Elenca gli sponsor e le informazioni sugli accordi che hanno con la squadra che ha vinto più tornei
Select *
FROM accordo
WHERE (Squadra = (Select Squadra_vincitrice	
			FROM edizioni_torneo
			Group by Squadra_vincitrice
			order by count(*) desc
			limit 1));

            
-- op12: Trova la squadra(o le squadre)che ha vinto meno tornei ma ha guadagnato di più
SELECT Squadra_vincitrice,sum(montepremi) as Vincite_totali, count(*) as Trofei
FROM torneo,edizioni_torneo
WHERE Nome = Torneo 
GROUP BY squadra_vincitrice
HAVING Vincite_totali = (SELECT max(vincite)
			FROM (SELECT Squadra_vincitrice as Squadra,sum(montepremi) as vincite FROM torneo,edizioni_torneo WHERE Nome = Torneo Group by Squadra) as s1,
            (SELECT Squadra_vincitrice as Squadra,count(*) as trofei FROM edizioni_torneo GROUP BY Squadra 
            HAVING trofei <= ALL (SELECT count(*) FROM edizioni_torneo GROUP BY Squadra_vincitrice)) as s2
            WHERE s1.Squadra = s2.Squadra);


-- op13: Trova il giocatore o i giocatori che hanno vinto più edizioni di un mondiale
SELECT Giocatore,count(*) as Mondiali_vinti
FROM vinto as v1,Torneo
WHERE Torneo = Nome AND tipo = 'Mondiale'
GROUP BY Giocatore
HAVING Mondiali_vinti >= all (SELECT count(*)
						FROM vinto as v1,Torneo
						WHERE Torneo = Nome AND tipo = 'Mondiale'
                        Group by Giocatore);


-- op14: Trova il giocatore, la squadra e lo stipendio,il cui è più alto di tutti gli altri giocatori della stessa squadra attuale
SELECT Giocatore,Squadra,Stipendio
FROM contratto as c1
WHERE Stipendio > all (SELECT Stipendio 
					   FROM contratto as c2
                       WHERE (c1.Giocatore <> c2.Giocatore) and (c1.Squadra = c2.Squadra) and (Scaduto = 0));
                      


-- Creazione Viste e query con viste

-- Vista giocatori ventenni --
CREATE or REPLACE VIEW Giocatori_ventenni(Nickname,Nome,Cognome,Età,Squadra, Data_inizio, Data_fine) 
AS SELECT Id, Nome, Cognome, year(CURDATE())-year(Data_di_nascita), Squadra, Data_inizio, Data_fine
FROM giocatore,contratto
WHERE Id = Giocatore AND Scaduto = 0
AND year(CURDATE())-year(Data_di_nascita) < 20;
Select * from Giocatori_ventenni;



-- Vista contenente tutti i proprietari di tutte le squadre
CREATE or REPLACE VIEW Proprietario(Id,Nome,Cognome,Data_nascita,Squadra,Stipendio)
as (SELECT Id,Nome,Cognome,Data_nascita,Squadra,Stipendio
	FROM staff
    Where Proprietario = 1
    Order by Squadra) ;
SELECT * from Proprietario;


-- Vista per informazioni numero trofei vinti dai giocatori
CREATE OR REPLACE VIEW Info_Trofei_Giocatori(Giocatore, Squadra, Num_trofei)
as (SELECT Giocatore, et.Squadra_vincitrice as Squadra, count(*)
	FROM vinto as v,torneo as t,edizioni_torneo as et
    WHERE (v.Torneo=et.Torneo and v.Anno = et.Anno) and t.Nome = et.Torneo
    group by Giocatore);
select * from Info_trofei_giocatori;



-- VISTA numero trofei vinti dalle squadre
CREATE or REPLACE VIEW Trofei_Squadra(Squadra,Tipo,Vittorie)
AS SELECT s.Nome,t.tipo,count(*)
FROM Squadra as s,Torneo as t, edizioni_torneo as et
where s.Nome=et.Squadra_vincitrice and t.Nome=et.Torneo
GROUP BY s.Nome,Tipo;


-- Query op2 con vista--
SELECT Nickname,Nome,Cognome,età,Squadra,rateo(Uccisioni,Morti,Assist) as Rateo
FROM giocatori_ventenni,statistiche
WHERE ((Nickname = statistiche.Giocatore)
and (rateo(Uccisioni,Morti,Assist) > 2.00)
and year(CURDATE()) - year(Data_inizio) <= 1);

-- Query 5 con vista "Proprietario"
SELECT s.*
from staff as s
WHERE ((s.Stipendio > all(SELECT Stipendio FROM Proprietario as p1 WHERE p1.Squadra <> s.Squadra))
OR (s.Stipendio > any(SELECT Stipendio FROM contratto as c WHERE c.Squadra = s.Squadra and Scaduto = 0)))
AND s.Proprietario = 0;


-- op6 con vista

SELECT *
FROM info_trofei_Giocatori
where Num_Trofei >= 2;


-- op7 con vista
SELECT Id,Vittorie,t.tipo
FROM allenatore as a,trofei_squadra as t
WHERE a.Squadra = t.Squadra 
AND ((Vittorie >= 1 and t.Tipo = 'Mondiale')
OR (Vittorie >= 2 and t.Tipo = 'Nazionale'));


-- op11 con vista TROFEI_Squadra DA MODIFICARE
Select accordo.*
FROM accordo , trofei_squadra as t
where accordo.Squadra=t.Squadra 
AND t.vittorie = (Select max(vittorie) from trofei_squadra);