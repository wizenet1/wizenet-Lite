package com.example.user.wizenet2;

/**
 * Created by doron on 17/03/2016.
 */
public class Call {

    String cid,cparentid,cfname,clname,cemail,cphone,ccell,ccompany;
    //LatLng latlng;
    public String getCcompany() {
        return ccompany;
    }
    public String getCfname() {
        return cfname;
    }

    public String getClname() {
        return clname;
    }

    public String getCemail() {
        return cemail;
    }

    public String getCphone() {
        return cphone;
    }

    public String getCcell() {
        return ccell;
    }
    public String getCID() {
        return cid;
    }
    public String getCParentID() {
        return cparentid;
    }
    public void setCfname(String cfname) {
        this.cfname = cfname;
    }
    public void setCcompany(String ccompany) {
        this.ccompany = ccompany;
    }
    public void setClname(String clname) {
        this.clname = clname;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone;
    }

    public void setCcell(String ccell) {
        this.ccell = ccell;
    }

    Call(String cfname, String clname, String cemail, String cphone, String ccell, String ccompany ){
        this.cfname = cfname;
        this.clname = clname;
        this.cemail = cemail;
        this.cphone = cphone;
        this.ccell = ccell;
        this.ccompany = ccompany;

    }
    Call(String cfname, String clname, String cemail, String cphone, String ccell, String ccompany, String cid ){
        this.cfname = cfname;
        this.clname = clname;
        this.cemail = cemail;
        this.cphone = cphone;
        this.ccell = ccell;
        this.ccompany = ccompany;
        this.cid = cid;

    }
    Call(String cfname, String clname, String cemail, String cphone, String ccell, String ccompany, String cid, String cparentid ){
        this.cfname = cfname;
        this.clname = clname;
        this.cemail = cemail;
        this.cphone = cphone;
        this.ccell = ccell;
        this.ccompany = ccompany;
        this.cid = cid;
        this.cparentid = cparentid;
    }
    @Override
    public String toString() {
        return "\n[cfname:"+this.cfname+"\nclname:"+this.clname + "\n,cemail:" + this.cemail+ "\n,cphone:" +
                this.cphone+ "\n,ccell:"+ this.ccell + "\n,ccompany:"+ this.ccompany +"]" ;
    }


    //public LatLng getLatlng() {return latlng;}
    //that's how we represent the customer.name in the values of spinner in mapsActivity
//    @Override
//    public String toString() {
//        return this.name;
//    }

}


