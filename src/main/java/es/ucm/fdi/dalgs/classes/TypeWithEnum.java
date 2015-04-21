package es.ucm.fdi.dalgs.classes;



public class TypeWithEnum {
	
	public enum TypeOfCompetence { General, Transversal, Basica, Especifica };


	private TypeOfCompetence type;
	
	public TypeWithEnum(){
		super();
	}

	public TypeWithEnum(TypeOfCompetence type) {
	    this.type = type;
	}

	public TypeOfCompetence getType() {
	    return type;
	}

	public void setType(TypeOfCompetence type) {
	    this.type = type;
	}
}
