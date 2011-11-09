package cityfinder

class City {
	String name
	String country
    static constraints = {
    	name blank : false
    	country blank : false
    }
}
