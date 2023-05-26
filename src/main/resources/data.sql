INSERT INTO customers (first_name, last_name, address, zip, city, phone_number, email)
VALUES ('Wouter', 'Oosterbroek', 'Tjalling Wagenaarstraat 189', '9203 SP', 'Drachten', '06-14567823', 'wouter@oosterbroek.nl'),
       ('Sophie', 'van Echten', 'Dirk Schaferplein 198', '3122 EC', 'Schiedam', '06-32248106', 'sophie88@gmail.com'),
       ('Marjolein', 'Piek', 'Valkenkamp 169', '3607 LK', 'Maarssen', '06-83529536', 'mpiek@gmail.com'),
       ('Jasper', 'van Nispen', 'Langeweg 178', '5801 XW', 'Venray', '06-14567823', 'jvnis@hotmail.com');

INSERT INTO tasks (description, work_performed, job_done)
VALUES ('Repareer kapotte vaatwasser', 'Onderdelen vervangen en vaatwasser gerepareerd. Getest om ervoor te zorgen dat het correct functioneert.', true),
       ('Installeer nieuwe oven in klantrestaurant', 'Nieuwe oven geassembleerd en geïnstalleerd volgens specificaties. Getest om ervoor te zorgen dat het correct functioneert.', true),
       ('Los probleem met koffiemachine op die koffie van lage kwaliteit produceert', 'Koffiemachine geïnspecteerd, gereinigd en ontkalkt en de molen vervangen. Getest om ervoor te zorgen dat het hoogwaardige koffie produceert.', true),
       ('Repareer kapotte friteuse', 'De oorzaak van het probleem opgespoord, dat een beschadigd verwarmingselement was, en vervolgens vervangen. Getest om ervoor te zorgen dat het opwarmt zoals het hoort.', true),
       ('Voer routinematig onderhoud uit aan koelunits', 'Koelunits schoongemaakt en geïnspecteerd, versleten onderdelen besteld.', false);

INSERT INTO files (filename, path_to_image, description)
VALUES ('bar.png', '/images/bar.png', 'Gerepareerde vaatwasser'),
       ('foo.png', '/images/foo.png', 'De nieuwe oven klaar voor gebruik');