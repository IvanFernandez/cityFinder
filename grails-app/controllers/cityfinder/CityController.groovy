package cityfinder

import org.springframework.dao.DataIntegrityViolationException

class CityController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def info() {
		def cityInstance = City.get(params.id)
        def cityEncoded = URLEncoder.encode(cityInstance.name)
		def appid = "foOF4CzV34EFIIW4gz1lx0Ze1em._w1An3QyivRalpXCK9sIXT5de810JWold3ApkdMdCrc-%22"
		def url = "http://where.yahooapis.com/v1/places.q('" + cityEncoded + "')?appid=" + appid;
		println "URLL " + url
  		def xml = url.toURL().text
  		println xml
  		def rss = new XmlSlurper().parseText(xml)
  		def woeid = rss.place.woeid

		def yahooWeather = "http://weather.yahooapis.com/forecastrss?w=" + woeid + "&u=c"
		println yahooWeather
		def yahooWeatherXML = yahooWeather.toURL().text
		def yrss = new XmlSlurper().parseText(yahooWeatherXML)
		def urlcode = yrss.channel.link.text()
		def list = urlcode.tokenize("//")
		if (list.size() > 0) {
			def dirtyCode = list.last()
			def code = dirtyCode.tokenize("_").first()
	    	println code
	    	flash.code = code
			render(view: "info", model: [cityInstance: cityInstance])
			return
		}
		else {
			redirect(action: "list")
		}
	}
	
	def index() {
		redirect(action: "list", params: params)
	}

	def list() {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		[cityInstanceList: City.list(params), cityInstanceTotal: City.count()]
	}

	def create() {
		[cityInstance: new City(params)]
	}

	def save() {
		def cityInstance = new City(params)
		if (!cityInstance.save(flush: true)) {
			render(view: "create", model: [cityInstance: cityInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [
			message(code: 'city.label', default: 'City'),
			cityInstance.id
		])
		redirect(action: "show", id: cityInstance.id)
	}

	def show() {
		def cityInstance = City.get(params.id)
		println cityInstance
		if (!cityInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'city.label', default: 'City'),
				params.id
			])
			redirect(action: "list")
			return
		}

		[cityInstance: cityInstance]
	}

	def edit() {
		def cityInstance = City.get(params.id)
		if (!cityInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'city.label', default: 'City'),
				params.id
			])
			redirect(action: "list")
			return
		}

		[cityInstance: cityInstance]
	}

	def update() {
		def cityInstance = City.get(params.id)
		if (!cityInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'city.label', default: 'City'),
				params.id
			])
			redirect(action: "list")
			return
		}

		if (params.version) {
			def version = params.version.toLong()
			if (cityInstance.version > version) {
				cityInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[
							message(code: 'city.label', default: 'City')]
						as Object[],
						"Another user has updated this City while you were editing")
				render(view: "edit", model: [cityInstance: cityInstance])
				return
			}
		}

		cityInstance.properties = params

		if (!cityInstance.save(flush: true)) {
			render(view: "edit", model: [cityInstance: cityInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [
			message(code: 'city.label', default: 'City'),
			cityInstance.id
		])
		redirect(action: "show", id: cityInstance.id)
	}

	def delete() {
		def cityInstance = City.get(params.id)
		if (!cityInstance) {
			flash.message = message(code: 'default.not.found.message', args: [
				message(code: 'city.label', default: 'City'),
				params.id
			])
			redirect(action: "list")
			return
		}

		try {
			cityInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [
				message(code: 'city.label', default: 'City'),
				params.id
			])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [
				message(code: 'city.label', default: 'City'),
				params.id
			])
			redirect(action: "show", id: params.id)
		}
	}
}
