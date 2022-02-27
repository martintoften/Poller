async function getServices() {
    try {
        let result = await fetch("http://localhost:8888/service")
        let response = await result.json()
        return response
    } catch(error) {
        return null
    }
}

async function addService(service) {
    try {
        let result = await fetch("http://localhost:8888/service", {
            method: "POST",
            headers: {"Content-type": "application/json" },
            body: JSON.stringify(service)
        })
        return result
    } catch(error) {
        return null
    }
}

async function deleteService(serviceName) {
    try {
        let result = await fetch(`http://localhost:8888/service?name=${serviceName}`, {
            method: "DELETE"
        })
        return result
    } catch(error) {
        return null
    }
}

export { getServices, addService, deleteService }