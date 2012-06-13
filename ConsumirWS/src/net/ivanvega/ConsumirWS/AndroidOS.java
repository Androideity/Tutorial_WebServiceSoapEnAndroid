package net.ivanvega.ConsumirWS;

public class AndroidOS {
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	private String nombre;
	private String version;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nombre + " (" + version + ")";
	}

}
