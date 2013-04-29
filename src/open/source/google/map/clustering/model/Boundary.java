package open.source.google.map.clustering.model;

import java.awt.Rectangle;

public class Boundary extends Rectangle
{        
    public Boundary() { }
    public Boundary(Boundary b)
    {            
        /*this.Minx = b.Minx;
        this.Miny = b.Miny;
        this.Maxx = b.Maxx;
        this.Maxy = b.Maxy;*/
    }


    /// <summary>
    /// Normalize lat and lon values to their boundary values
    /// </summary>
    public void normalize()
    {
        //Minx = Minx.NormalizeLongitude();
        //Maxx = Maxx.NormalizeLongitude();
        //Miny = Miny.NormalizeLatitude();
        //Maxy = Maxy.NormalizeLatitude();
    }


    public void validateLatLon()
    {
        /*if ((Minx > LatLonInfo.MaxLonValue || Minx < LatLonInfo.MinLonValue)
            || (Maxx > LatLonInfo.MaxLonValue || Maxx < LatLonInfo.MinLonValue)
            || (Miny > LatLonInfo.MaxLatValue || Miny < LatLonInfo.MinLatValue)
            || (Maxy > LatLonInfo.MaxLatValue || Maxy < LatLonInfo.MinLatValue)
            )
            throw new IllegalArgumentException("input Boundary.ValidateLatLon() error " + this);*/
    }

    // Distance lon
    //public double AbsX { get { return Minx.AbsLon(Maxx); } }

    // Distance lat
    //public double AbsY { get { return Miny.AbsLat(Maxy); } }
}
