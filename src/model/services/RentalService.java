package model.services;

import model.entities.CarRental;
import model.entities.Invoice;

public class RentalService {

	private Double pricePerDay;
	private Double pricePerHour;

	
	/*Porque instanciar um TaxService ao invez do brasilTaxService ?
	 * Por quest�o de manuten��o, ou seja, toda vez que for criar um servi�o de aluguel de carro
	 * ao invez de instanciar pa�s a pa�s voc� chama a classe GERAL que de acordo com pa�s a pa�s define as regras
	 * S� muda na hora do programa principal, onde voc� define as taxas e chama a class de acordo.
	 * */
	
	private TaxService taxService;

	public RentalService(Double pricePerDay, Double pricePerHour, TaxService taxService) {
		this.pricePerDay = pricePerDay;
		this.pricePerHour = pricePerHour;
		this.taxService = taxService;
	}

	public void processInvoice(CarRental carRental) {
		// carRental ta pegando a data de inicio(t1) e data fim(t2) e pegando os
		// milissegundos
		long t1 = carRental.getStart().getTime();
		long t2 = carRental.getFinish().getTime();
		double hours = (double) (t2 - t1) / 1000 / 60 / 60; // convers�o de milissegundo para Horas, o double � para pegar tamb�m os minutos

		double basicPayment;
		if (hours <= 12) {
			basicPayment = Math.ceil(hours) * pricePerHour;
		} else {
			// Acha quantos dias
			basicPayment = Math.ceil(hours / 24) * pricePerDay;
		}
		
		double tax = taxService.tax(basicPayment);
		carRental.setInvoice(new Invoice(basicPayment, tax));
	}

}
