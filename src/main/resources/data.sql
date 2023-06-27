INSERT INTO customers (first_name, last_name, address, zip, city, phone_number, email)
VALUES ('Wouter', 'Oosterbroek', 'Tjalling Wagenaarstraat 189', '9203 SP', 'Drachten', '06-14567823', 'wouter@oosterbroek.nl'),
       ('Sophie', 'van Echten', 'Dirk Schaferplein 198', '3122 EC', 'Schiedam', '06-32248106', 'sophie88@gmail.com'),
       ('Marjolein', 'Piek', 'Valkenkamp 169', '3607 LK', 'Maarssen', '06-83529536', 'mpiek@gmail.com'),
       ('Jasper', 'van Nispen', 'Langeweg 178', '5801 XW', 'Venray', '06-14567823', 'jvnis@hotmail.com'),
       ('Sanne', 'de Wit', 'Burg. Hazenbergerstraat 265', '1812 VE', 'Alkmaar', '06-12345678', 'sannedew@gmail.com'),
       ('Niels', 'van der Meer','H.C. Kruizingastraat 55', '7335 RZ', 'Apeldoorn', '06-87654321', 'nielsvm@hotmail.com'),
       ('Daan', 'de Boer', 'Jan Evertsenstraat 82', '9851 JB', 'Leeuwarden', '06-56789012', 'daandeboer@gmail.com'),
       ('Romy', 'Bakker', 'Zuidwal 56', '7208 PB', 'Zutphen', '06-23456789', 'romy@hotmail.com');

INSERT INTO tasks (description, work_performed, job_done, customer_id)
VALUES ('Repareer kapotte vaatwasser', 'Onderdelen vervangen en vaatwasser gerepareerd. Getest om ervoor te zorgen dat het correct functioneert.', true, 1),
       ('Installeer nieuwe oven in klantrestaurant', 'Nieuwe oven geassembleerd en geïnstalleerd volgens specificaties. Getest om ervoor te zorgen dat het correct functioneert.', true, 2),
       ('Los probleem met koffiemachine op die koffie van lage kwaliteit produceert', 'Koffiemachine geïnspecteerd, gereinigd en ontkalkt en de molen vervangen. Getest om ervoor te zorgen dat het hoogwaardige koffie produceert.', true, 3),
       ('Repareer kapotte friteuse', 'De oorzaak van het probleem opgespoord, dat een beschadigd verwarmingselement was, en vervolgens vervangen. Getest om ervoor te zorgen dat het opwarmt zoals het hoort.', true, 4),
       ('Voer routinematig onderhoud uit aan koelunits', 'Koelunits schoongemaakt en geïnspecteerd, versleten onderdelen besteld.', false, 4),
       ('Vervang kapotte ovenlamp', 'Ovenlamp vervangen', true, 5),
       ('Reinig industriële afwasmachine',  'Afwasmachine gereinigd en gecontroleerd', false, 6),
       ('Vervang koelvloeistof in koelcel', 'Koelvloeistof vervangen en testen of koelcel goed werkt', true, 7),
       ('Vervang versleten deurrubbers', 'Versleten deurrubbers vervangen', true, 8),
       ('Reinig frituur', 'Frituur gereinigd', false, 1),
       ('Vervang onderdeel koffiezetapparaat', 'Onderdeel vervangen ', true, 2),
       ('Test hogedrukreiniger','Hogedrukreiniger getest', false, 3),
       ('Reinig afzuigkap','Afzuigkap gereinigd', false, 4),
       ('Test temperatuur koelkast', 'Temperatuur koelkast gecontroleerd', false, 5),
       ('Vervang versleten onderdeel in keukenmachine', 'Onderdeel vervangen', true, 6),
       ('Reinig broodrooster', 'Broodrooster gereinigd', false, 7),
       ('Vervang lampen in diepvrieskast', 'Lampen in diepvrieskast vervangen ', true, 8);

INSERT INTO schedule_tasks (date, start_time, end_time, task_id)
VALUES ('2022-08-01', '13:00:00', '16:00:00', 1),
       ('2023-08-01', '10:00:00', '12:00:00', 2),
       ('2023-08-02', '10:00:00', '12:00:00', 4),
       ('2023-08-02', '14:00:00', '16:00:00', 5),
       ('2023-08-03', '09:00:00', '11:00:00', 3);

INSERT INTO users (username, password, enabled, apikey, email)
VALUES ('John', '$2a$12$vL/WjHUF7ZL5bYDQNfCkS.C.eKpd/xZlAzAgeO5ItC9kNBHyajzbW', true, '7847493', 'test@testy.nl'),
       ('Jane', '$2a$12$rCZi4UIXPtrNdQdSfelAcOwweREMl4.AddVv3WOKvl.2U4dIWta9K', true, '7847493', 'test@testy.nl'),
       ('Mike', '$2a$12$/bn9V1Rs3ddYvJaqu10QwO/oU3BIiWpYtPzutAdz29dGilUSKQSou', true, '7847493', 'test@testy.nl');
-- Alle passwords zijn -> password

INSERT INTO authorities (username, authority)
VALUES ('John', 'ROLE_ADMIN'),
       ('Jane', 'ROLE_PLANNER'),
       ('Mike', 'ROLE_MECHANIC');

--todo: niet vergeten inlog voor admin toe te voegen, noem deze gebruikers in de documentatie ook met het plain text-password erbij en niet alleen de encrypted versie