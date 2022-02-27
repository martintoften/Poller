import { useEffect, useState } from 'react';
import './App.css';
import { getServices, addService, deleteService } from "./ServicePollerRepository"

function App() {
  const [services, setServices] = useState([])
  const [newService, setNewService] = useState({ name: "", url: "" })

  useEffect(() => {
    const fetchServices = async () => {
      const data = await getServices();
      if (data) {
        setServices(data);
      }
    }
    setInterval(() => {
      fetchServices()
    }, 5000)
    fetchServices()
  }, [])

  async function deleteServiceAndGetUpdatedList(serviceName) {
    let result = await deleteService(serviceName)
    if (result == null) {
      return
    }
    let services = await getServices()
    setServices(services)
  }

  async function addServiceAndGetUpdatedList(service) {
    let result = await addService(service)
    if (result == null) {
      return
    }
    let services = await getServices()
    setServices(services)
  }

  function formatDate(date) {
    return new Date(Date.parse(date)).toLocaleDateString()
  }

  return (
    <div className="App">
      <h3>Service poller</h3>
      <input type="text" name="name" className="Text-input" placeholder="Service name" onChange={(event) => setNewService({ ...newService, name: event.target.value })} />
      <input type="text" name="url" className="Text-input" placeholder="Url" onChange={(event) => setNewService({ ...newService, url: event.target.value })} />
      <input type="submit" value="Add service" className="Submit-input" onClick={() => addServiceAndGetUpdatedList(newService)} />
      <div className="Service-list">{services.map(service => {
        return (
          <li className="Service-item" key={service.name}>
            <div className="Service-item-top">
              <p>{service.name + "(" + service.state + ")"}</p>
              <img className="Bin" src={"./bin.png"} width="16" height="16" onClick={() => deleteServiceAndGetUpdatedList(service.name)} />
            </div>
            <p>{service.url}</p>
            <p>{formatDate(service.createdAt)}</p>
          </li>)
      })}
      </div>
    </div>
  );
}

export default App;
