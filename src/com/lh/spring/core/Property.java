/**
 * 
 */
package com.lh.spring.core;

/**
 * @author lh
 * @data 2021年1月17日
 * Email 2944862497@qq.com
 */
public class Property {
	
	private String name;
	private String value;
	private String ref;
	@Override
	public String toString() {
		return "Property [name=" + name + ", value=" + value + ", ref=" + ref + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((ref == null) ? 0 : ref.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Property other = (Property) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (ref == null) {
			if (other.ref != null)
				return false;
		} else if (!ref.equals(other.ref))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 
	 */
	public Property() {
		super();
	}
	/**
	 * @param name
	 * @param value
	 * @param ref
	 */
	public Property(String name, String value, String ref) {
		super();
		this.name = name;
		this.value = value;
		this.ref = ref;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	
	

}
