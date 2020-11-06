package edu.hfu.scre.entity.xmlBean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.FIELD)
public class SysDictionary {
	@XmlAttribute(name ="dictId")
	private Integer dictId;
	@XmlAttribute
	private String dictKey;
	@XmlAttribute
	private String dictValue;
	@XmlAttribute
	private String dictValue1;
	@XmlAttribute
	private String dictType; // 分类
}
