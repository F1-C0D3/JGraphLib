package de.jgraphlib.util.treeparser;

public class Key extends Element{ 
	public final String string;
	
	public Key(String string) {
		this.string = string;
	}
			
	@Override
	public String toString() {
		return this.string;
	}
	
	@Override
	public boolean equals(Object object) {
		
        if (object == this)
        	return true; 

        if (!(object instanceof Key))  
            return false; 
            
        Key key = (Key) object; 
    
        return this.string.equals(key.toString());		
	}
}
