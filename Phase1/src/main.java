import java.io.File;
import java.math.BigInteger;

import generated_laura.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.Scanner;

public class main {
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);

		int eingabe = -1;
		while (eingabe != 0) {

			if (eingabe == -1) {
				System.out.println("Herzlich Willkommen! Was möchten Sie tun?");
				System.out
						.println("Möchten Sie sich ein Rezept anschauen, drücken Sie bitte die 1!");
				System.out
						.println("Möchten Sie ein Kommentar zu einem Rezept hinzufügen, drücken Sie bitte die 2!"
								+ "\r\n");
				eingabe = sc.nextInt();
			}

			if (eingabe == 1) {
				ausgabe();
				eingabe = 0;
			}

			else if (eingabe == 2) {
				kommentar();
				eingabe = 0;
				// HIER MUSS DANN DIE KOMMENTARFUNKTION REIN!!!
			}

			else // Wenn ungültige EIngabe!!!
			{
				eingabe = -1;
				System.out
						.println("Die Eingabe ist ungültig! Bitte geben Sie entweder eine 1 oder 2 ein");// neustart
			}
		}
	}

	public static void ausgabe() throws Exception {

		// Vorgaben durch Erstellung
		JAXBContext jc = JAXBContext.newInstance("generated_laura");
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		RezeptSammlung rs = (RezeptSammlung) unmarshaller.unmarshal(new File(
				"src/AUfgabe3d.xml"));

		// Allgemeine Angaben zum Rezept
		System.out.println("Beschreibung des Kochbuches: " + "\r\n"
				+ rs.getBeschreibung() + "\r\n");
		for (int i = 0; i < rs.getRezept().size(); i++) {
			if (rs.getRezept().get(i) instanceof Rezept) {
				Rezept r = (Rezept) rs.getRezept().get(i);
				System.out
						.println("_______________________________________________________________________________");

				// Ausgabe des n-ten Rezepts
				System.out.println("\r\n" + "Name: " + r.getRezeptname());
				System.out.println("Rezeptnummer: "
						+ r.getIdRezept().toString());
				System.out.println("Anmerkungen zum Rezept: "
						+ r.getAnmerkungen());
				System.out.println("Brennwert in kcal: " + r.getBrennwert());

				// Ausgabe der Zutaten!
				System.out.println("\r\n" + "Zutaten:");
				for (int j = 0; j < r.getZutaten().getZutat().size(); j++) { // solange wie es Zutaten gibt
					Zutat z = (Zutat) r.getZutaten().getZutat().get(j);
					System.out.println("" + z.getContent() + ": "
							+ z.getMenge() + " " + z.getEinheit());
				}

				// Ausgabe der Zubereitung
				System.out.println("\r\n" + "Zubereitung:");
				for (int k = 0; k < r.getZubereitung().getStep().size(); k++) { // solange wie es steps gibt
					Step s = (Step) r.getZubereitung().getStep().get(k);
					System.out.println(s.getContent());
				}

			}// Beendung der If-Bedingung
		}// Beendung der for-Schleife
	}// Beendung der Methode

	public static void kommentar() throws Exception {

		//Vorgabe 
		JAXBContext jc = JAXBContext.newInstance("generated_laura");
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		RezeptSammlung rs = (RezeptSammlung) unmarshaller.unmarshal(new File("src/Aufgabe3d.xml"));
		Rezept r;
		
		Scanner sc = new Scanner(System.in);
		int RezeptAuswahl;
		int KommentarIndex = 0;
		String kommentarString;
		Kommentar kommentar = new Kommentar();
		BigInteger kommentarIndexBigInteger;

		System.out.println("Zu welchem Rezpet wollen Sie ein Kommentar verfassen? Geben Sie bitte die Rezeptnummer ein: ");
		
		//Ausgabe der Rezeptnamen mit Nummern - Zur EingabeHilfe für den Nutzer
		for (int i = 0; i < rs.getRezept().size(); i++) {
			r = (Rezept) rs.getRezept().get(i);
			System.out.println(r.getIdRezept() +" " + r.getRezeptname());

		}
		//Einlesen der Eingabe
		RezeptAuswahl = sc.nextInt();
		sc.nextLine();


		for (int j = 0; j < rs.getRezept().get(RezeptAuswahl-1).getKommentare().getKommentar().size(); j++){
			
			int y = rs.getRezept().get(RezeptAuswahl-1).getKommentare().getKommentar().get(j).getIdKommentar().intValue();
			
			if (KommentarIndex < y) {
				KommentarIndex = y;
			}
		}
		kommentarIndexBigInteger = BigInteger.valueOf(KommentarIndex+1);
		System.out.println("Schreiben Sie ein Kommentar: ");
		kommentarString = sc.nextLine();
		
		
		kommentar.setContent(kommentarString);
		kommentar.setIdKommentar(kommentarIndexBigInteger);
		rs.getRezept().get(RezeptAuswahl-1).getKommentare().getKommentar().add(kommentar);

		//Einfügen in die XML - marshallen
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		//Output
		marshaller.marshal(rs, (new File("src/Aufgabe3d.xml")));
		

	}
}
