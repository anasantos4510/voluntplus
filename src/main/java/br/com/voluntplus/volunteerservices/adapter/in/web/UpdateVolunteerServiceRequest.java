package br.com.voluntplus.volunteerservices.adapter.in.web;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Schema(description = "Dados cadastrais completos ou uma alteração isolada de status")
public final class UpdateVolunteerServiceRequest {

	private String name;
	private String descricao;
	private String modalities;
	private String idCategoria;
	private String providerImage;
	private List<String> diaDaSemana;
	private List<String> turno;
	private String cep;
	private String estado;
	private String cidade;
	private String bairro;
	private Integer tipoLocalizacao;
	private String whatsapp;
	private String telefone;
	private String instagram;
	private String site;
	private String status;
	private final Set<String> providedProperties = new LinkedHashSet<>();
	private final Set<String> unexpectedProperties = new LinkedHashSet<>();

	@JsonSetter("name")
	public void setName(String name) {
		this.name = name;
		providedProperties.add("name");
	}

	@JsonSetter("descricao")
	public void setDescricao(String descricao) {
		this.descricao = descricao;
		providedProperties.add("descricao");
	}

	@JsonSetter("modalities")
	public void setModalities(String modalities) {
		this.modalities = modalities;
		providedProperties.add("modalities");
	}

	@JsonSetter("idCategoria")
	public void setIdCategoria(String idCategoria) {
		this.idCategoria = idCategoria;
		providedProperties.add("idCategoria");
	}

	@JsonSetter("providerImage")
	public void setProviderImage(String providerImage) {
		this.providerImage = providerImage;
		providedProperties.add("providerImage");
	}

	@JsonSetter("diaDaSemana")
	public void setDiaDaSemana(List<String> diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
		providedProperties.add("diaDaSemana");
	}

	@JsonSetter("turno")
	public void setTurno(List<String> turno) {
		this.turno = turno;
		providedProperties.add("turno");
	}

	@JsonSetter("cep")
	public void setCep(String cep) {
		this.cep = cep;
		providedProperties.add("cep");
	}

	@JsonSetter("estado")
	public void setEstado(String estado) {
		this.estado = estado;
		providedProperties.add("estado");
	}

	@JsonSetter("cidade")
	public void setCidade(String cidade) {
		this.cidade = cidade;
		providedProperties.add("cidade");
	}

	@JsonSetter("bairro")
	public void setBairro(String bairro) {
		this.bairro = bairro;
		providedProperties.add("bairro");
	}

	@JsonSetter("tipoLocalizacao")
	public void setTipoLocalizacao(Integer tipoLocalizacao) {
		this.tipoLocalizacao = tipoLocalizacao;
		providedProperties.add("tipoLocalizacao");
	}

	@JsonSetter("whatsapp")
	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
		providedProperties.add("whatsapp");
	}

	@JsonSetter("telefone")
	public void setTelefone(String telefone) {
		this.telefone = telefone;
		providedProperties.add("telefone");
	}

	@JsonSetter("instagram")
	public void setInstagram(String instagram) {
		this.instagram = instagram;
		providedProperties.add("instagram");
	}

	@JsonSetter("site")
	public void setSite(String site) {
		this.site = site;
		providedProperties.add("site");
	}

	@JsonSetter("status")
	public void setStatus(String status) {
		this.status = status;
		providedProperties.add("status");
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
	boolean statusWasProvided() { return providedProperties.contains("status"); }
	boolean dataPropertyWasProvided() { return providedProperties.stream().anyMatch(name -> !"status".equals(name)); }
	Set<String> unexpectedProperties() { return Set.copyOf(unexpectedProperties); }
}
