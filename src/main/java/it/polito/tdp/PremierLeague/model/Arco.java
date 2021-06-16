package it.polito.tdp.PremierLeague.model;

public class Arco implements Comparable<Arco>{

	private Player source;
	private Player destination;
	private Integer peso;
	
	public Arco(Player source, Player destination, Integer peso) {
		this.source = source;
		this.destination = destination;
		this.peso = peso;
	}

	public Player getSource() {
		return source;
	}

	public void setSource(Player source) {
		this.source = source;
	}

	public Player getDestination() {
		return destination;
	}

	public void setDestination(Player destination) {
		this.destination = destination;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(Arco other) {
		return other.peso.compareTo(this.peso);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arco other = (Arco) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}
	
}
