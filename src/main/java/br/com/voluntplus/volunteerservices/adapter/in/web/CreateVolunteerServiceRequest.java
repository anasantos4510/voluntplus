package br.com.voluntplus.volunteerservices.adapter.in.web;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Schema(description = "Dados para cadastrar um serviço voluntário")
public final class CreateVolunteerServiceRequest {

	private final String name;
	private final String descricao;
	private final String modalities;
	private final String idCategoria;
	@Schema(
			description = "Data URL opcional em PNG, JPEG ou WEBP, com conteúdo decodificado de até 5 MB",
			nullable = true)
	private final String providerImage;
	private final List<String> diaDaSemana;
	private final List<String> turno;
	private final String cep;
	private final String estado;
	private final String cidade;
	private final String bairro;
	private final Integer tipoLocalizacao;
	private final String whatsapp;
	private final String telefone;
	private final String instagram;
	private final String site;
	private final String status;
	private final Set<String> unexpectedProperties = new LinkedHashSet<>();

	@JsonCreator
	public CreateVolunteerServiceRequest(
			@JsonProperty("name") String name,
			@JsonProperty("descricao") String descricao,
			@JsonProperty("modalities") String modalities,
			@JsonProperty("idCategoria") String idCategoria,
			@JsonProperty("providerImage") String providerImage,
			@JsonProperty("diaDaSemana") List<String> diaDaSemana,
			@JsonProperty("turno") List<String> turno,
			@JsonProperty("cep") String cep,
			@JsonProperty("estado") String estado,
			@JsonProperty("cidade") String cidade,
			@JsonProperty("bairro") String bairro,
			@JsonProperty("tipoLocalizacao") Integer tipoLocalizacao,
			@JsonProperty("whatsapp") String whatsapp,
			@JsonProperty("telefone") String telefone,
			@JsonProperty("instagram") String instagram,
			@JsonProperty("site") String site,
			@JsonProperty("status") String status) {
		this.name = name;
		this.descricao = descricao;
		this.modalities = modalities;
		this.idCategoria = idCategoria;
		this.providerImage = providerImage;
		this.diaDaSemana = diaDaSemana;
		this.turno = turno;
		this.cep = cep;
		this.estado = estado;
		this.cidade = cidade;
		this.bairro = bairro;
		this.tipoLocalizacao = tipoLocalizacao;
		this.whatsapp = whatsapp;
		this.telefone = telefone;
		this.instagram = instagram;
		this.site = site;
		this.status = status;
	}

	@JsonAnySetter
	public void rejectUnexpectedProperty(String propertyName, Object ignoredValue) {
		unexpectedProperties.add(propertyName);
	}

	String name() { return name; }
	String descricao() { return descricao; }
	String modalities() { return modalities; }
	String idCategoria() { return idCategoria; }
	String providerImage() { return providerImage; }
	List<String> diaDaSemana() { return diaDaSemana; }
	List<String> turno() { return turno; }
	String cep() { return cep; }
	String estado() { return estado; }
	String cidade() { return cidade; }
	String bairro() { return bairro; }
	Integer tipoLocalizacao() { return tipoLocalizacao; }
	String whatsapp() { return whatsapp; }
	String telefone() { return telefone; }
	String instagram() { return instagram; }
	String site() { return site; }
	String status() { return status; }
	Set<String> unexpectedProperties() { return Set.copyOf(unexpectedProperties); }
}
