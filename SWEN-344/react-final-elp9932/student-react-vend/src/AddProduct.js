import React from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Input, Form, FormGroup, Button} from 'reactstrap';
import './vend.css';

class AddProduct extends React.Component
{

    constructor(props) {
        super(props);

        this.state = {
            name:"",
            price:"0",
            quantity:"0",
            button:"A",
            editModal: false,
            openDropdown: false
        };
        this.nameChanged = this.nameChanged.bind(this);
        this.priceChanged = this.priceChanged.bind(this);
        this.quantityChanged = this.quantityChanged.bind(this);
        this.buttonChanged = this.buttonChanged.bind(this);
    }

    openModal() {
        this.setState({
            editModal: !this.state.editModal,
        });
    }

    openDropdown() {
        this.setState({
            openDropdown: !this.state.openDropdown,
        });
    }


    nameChanged(event) {
        this.setState({name: event.target.value});
    }
    priceChanged(event) {
        this.setState({price: event.target.value});
    }
    quantityChanged(event) {
        let isnum = /^\d+$/.test(event.target.value);
        if(isnum||(event.target.value==="")){
            this.setState({quantity: event.target.value});  
        }
    }
    buttonChanged(event) {
        this.setState({button: event.target.value});
    }


    createProduct(){
        if((this.state.name.trim()==="")||(this.state.price.trim()==="")||(this.state.quantity.trim()==="")){
            alert("Please fill in all fields properly. Fields should not be empty or containing invalid characters in the numeric fields");
        }else if((Number(this.state.quantity.trim())<=0)||(Number(this.state.quantity.trim())>20)){
            alert("Please input correct values for quanity(note: must be between 0 and 20)")
        }
        else if((Math.round(Number(this.state.price.trim())*100)/100)===0){//cursed method of handling number checking. can probably be simplified but if it aint broke...
            alert("Please make sure price is not 0(or rounding to 0.00)")
        }
        else{
            this.props.onClick(this.state.name, this.state.price, this.state.quantity, this.state.button);
            this.wipeState();
        }
    }

    wipeState(){
        this.setState({
            name:"",
            price:"0",
            quantity:"0",
            button:"A",
            editModal: false,
            openDropdown: false
        });
    }

    render()
    {
        return (
            <div className='customContainer'>            
            <Button id="newProduct" onClick={() => { this.openModal() }}>Add item</Button>
            <Modal isOpen={this.state.editModal} toggle={this.toggleEdit}>
                    <ModalHeader toggle={this.toggleEdit}>Add a new vending item(must replace an existing vending item)</ModalHeader>
                    <ModalBody>
                    Name: <Input value={this.state.name} placeholder='Product Name' type='text' onChange={this.nameChanged} />
                    Price: <Input value={this.state.price} placeholder='Product Price' type='number' step='0.01' onChange={this.priceChanged} />
                    Quantity: <Input value={this.state.quantity} placeholder='Product Quantity' type='number' min='1' max='20' onChange={this.quantityChanged} />
                    Selection (button): <Form>
                        <FormGroup>
                            <Input id="exampleSelect" name="select" type="select" onChange={this.buttonChanged}>
                                <option>A</option>
                                <option>B</option>
                                <option>C</option>
                                <option>D</option>
                                <option>E</option>
                                <option>F</option>
                                <option>G</option>
                                <option>H</option>
                            </Input>
                        </FormGroup>
                    </Form>
                    </ModalBody>
                    <ModalFooter>
                        <Button onClick={() => { this.createProduct() }} type="button">Save</Button>
                        <Button onClick={() => { this.wipeState() }} type="button">Cancel</Button>
                    </ModalFooter>
                </Modal>
            </div>

        );
    }
}

export default AddProduct;