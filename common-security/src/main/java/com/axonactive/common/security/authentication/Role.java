package com.axonactive.common.security.authentication;
/**
 * 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  the name of the role. it's mandatory and unique
	 */
	private String name;

	/**
	 * the  name that will be displayed on the GUI of the role. it's optional.
	 * If it's null, the name will be used to display on the GUI
	 */
	private String displayName;

	/**
	 * access rights linked to the role. A role will have its own permissions and its parent's permissions
	 */
	private List<Permission> permissions;

	/**
	 * its parent role
	 */
	private Role parent;

	/**
	 * its children roles
	 */
	private List<Role> children;

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Role parent) {
		this.parent = parent;
		this.parent.addChild(this);
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Role> children) {
		this.children = children;
		if (this.children == null) {
			this.children = new ArrayList<>();
		}
		
		for (Role role : children) {
			role.setParent(this);
		}
	}

	public void addChild(Role childRole) {
		if (this.children == null) {
			this.children = new ArrayList<>();
		}
		this.children.add(childRole);
	}

}
