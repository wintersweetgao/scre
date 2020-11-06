package edu.hfu.scre.entity.xmlBean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.hfu.scre.entity.xmlBean.SysDictionary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "SysDictionary")
@XmlAccessorType(XmlAccessType.FIELD)
public class SysDictionaryList {
	@XmlElement(name = "item")
	private List<SysDictionary>  dictList;
}
