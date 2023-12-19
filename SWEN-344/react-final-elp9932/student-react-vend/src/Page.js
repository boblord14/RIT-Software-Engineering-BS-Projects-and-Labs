import React from 'react';
import {Container} from 'reactstrap';
import './vend.css';

import Keys from './Keys';
import Product from './Product';
import AddProduct from './AddProduct';

export class product {
    constructor(name, price, quantity, button) {
      this.name = name;
      this.price = price;
      this.quantity = quantity;
      this.button = button;
    }
  }


class Page extends React.Component
{

    constructor(props)
    {
        super(props);
        this.state = {list: []};
    }



    buttonSelect=(key)=>
    {
        this.putData(key, null);

    }

    updateData=(data)=>
    {
        this.setState({list: data});
    }

    addNewProduct=(name, price, quantity, button)=>
    {
        var newProduct = new product(name, price, quantity, button);
        this.createData(newProduct);
    }

    //This method makes the API call to retrieve the data from the server using RESTful API
    fetchData = () => {
        //With Flask CORS enabled, we can directly call the server on port 5000
        fetch('http://localhost:5000/vend')
         .then( 
             (response) => 
             {
                return response.json() ;
             }
             )//The promise response is returned, then we extract the json data
         .then (jsonOutput => //jsonOutput now has result of the data extraction
                  {
                     this.updateData(jsonOutput)
                    }
              )
         .catch((error => console.log("**Fetch exception:" + error)))
      }

      putData = (buttonPress, updateMachine) => {
        if(updateMachine === null){
            var jsonData = JSON.stringify({buttonPress});
        }else{
            var jsonData = JSON.stringify({updateMachine});
        }

        var hdr = {'content-type': 'application/json'}
        fetch(`http://localhost:5000/vend`, {method: 'PUT', body: jsonData, headers: hdr})
        .then(
            (response) => 
            {
               if (response.status !== 200)
               {
                   console.log("HTTP error:" + response.status + ":" +  response.statusText);
                   return ([ ["status ", response.status]]);
               }
            }
            )
          .then(()=>{this.fetchData();}
            
          )
       .catch((error) => 
               {console.log(error);} )
      }


      createData = (newProduct) => {
        var jsonData = JSON.stringify({newProduct});
        var hdr = {'content-type': 'application/json'}
        fetch(`http://localhost:5000/vend`, {method: 'POST', body: jsonData, headers: hdr})
        .then(
            (response) => 
            {
               if (response.status !== 200)
               {
                   console.log("HTTP error:" + response.status + ":" +  response.statusText);
                   return ([ ["status ", response.status]]);
               }
            }
            )
          .then(()=>{
            this.putData(newProduct.button, newProduct);
        }
          )
       .catch((error) => 
               {console.log(error);} )
      }

    //When the component is loaded, this will call the fetchData method to retrieve the data
    componentDidMount(){
        this.fetchData();
    } 
    render()
    {
        console.log(this.state.list);
        if (this.state.list.length > 0)
        {
        return(
        <div>
        <h3 className='flex-container centered mx-auto'>Mini Vend</h3>
            <Product  productData={this.state.list}/>
        <Keys callback={this.buttonSelect}></Keys>
        <AddProduct onClick={this.addNewProduct}/>
        </div>
        )
        }
        else
        return(
        <div>
            <Container className='centered'>
            <h2>No data available</h2>
            <img src='./img/ded.png' alt='dead emoji'></img>
            <h3>Make sure the server is up and running</h3>
            </Container>
            <Keys callback={this.buttonSelect}></Keys>
        </div>
            )
    }
}

export default Page;