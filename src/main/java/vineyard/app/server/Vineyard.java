package vineyard.app.server;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Vineyards object for hibernate layer model.
 */
@Entity
@Table(name = "vineyards")
public class Vineyard {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "gid")
    private Long gid;

    @Column(name = "id")
    private Long id;

    @Column(name = "length")
    private Float length;
	
    @Type(type = "org.hibernatespatial.GeometryUserType")
    @Column(name = "the_geom")
    private Geometry geometry;
	
    public Long getGid() {
        return gid;
    }
	
    public void setGid(Long gid) {
        this.gid = gid;
    }
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getLength() {
        return length;
    }
	
    public void setLength(Float length) {
        this.length = length;
    }
	
    public Geometry getGeometry() {
        return geometry;
    }
	
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
	
}
