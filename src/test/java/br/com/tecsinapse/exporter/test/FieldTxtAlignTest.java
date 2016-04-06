package br.com.tecsinapse.exporter.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import br.com.tecsinapse.exporter.txt.FieldTxt;
import br.com.tecsinapse.exporter.txt.FieldTxtAlign;

public class FieldTxtAlignTest {

	@Test
	public void deveAlinharTextoDireita() {
		FieldTxt field = FieldTxt.newBuilder()
				.withAlign(FieldTxtAlign.RIGHT)
				.withContent("12345")
				.withFixedSize(10)
				.build();

		String valor = FieldTxtAlign.RIGHT.getValueAligned(field);
		Assert.assertEquals(valor, "     12345");
	}

	@Test
	public void deveAlinharTextoEsquerda() {
		FieldTxt field = FieldTxt.newBuilder()
				.withAlign(FieldTxtAlign.LEFT)
				.withContent("12345")
				.withFixedSize(10)
				.build();

		String valor = FieldTxtAlign.LEFT.getValueAligned(field);
		Assert.assertEquals(valor, "12345     ");
	}

	@Test
	public void deveAlinharTextoDireitaPreenchendoComZero() {
		FieldTxt field = FieldTxt.newBuilder()
				.withAlign(FieldTxtAlign.RIGHT)
				.withContent("12345")
				.withFixedSize(10)
				.withFiller("0")
				.build();

		String valor = FieldTxtAlign.RIGHT.getValueAligned(field);
		Assert.assertEquals(valor, "0000012345");
	}

	@Test
	public void deveAlinharTextoEsquerdaPreenchendoComZero() {
		FieldTxt field = FieldTxt.newBuilder()
				.withAlign(FieldTxtAlign.LEFT)
				.withContent("12345")
				.withFixedSize(10)
				.withFiller("0")
				.build();

		String valor = FieldTxtAlign.LEFT.getValueAligned(field);
		Assert.assertEquals(valor, "1234500000");
	}

	@Test
	public void deveCortarTextoMaiorQueDefinido() {
		FieldTxt field = FieldTxt.newBuilder()
				.withAlign(FieldTxtAlign.LEFT)
				.withContent("12345")
				.withFixedSize(3)
				.build();

		String valor = FieldTxtAlign.LEFT.getValueAligned(field);
		Assert.assertEquals(valor, "123");
	}

}

