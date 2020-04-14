import React from 'react'
import {ELEMENT_TYPE} from '../constants/contants'
import {ElementDictionary} from './FormElementComponent'

const FormElementListComponent = (props) => {
  const {element} = props;
  const MyReactElement = ElementDictionary[element.type];

  const handleChange = (event) => {
    const {setValue, elementKey} = props;
    const value = getCurrentValue(event);
    setValue(elementKey, value);
  }

  const getCurrentValue = (event) =>{

    const {element} = props;

    switch (element.type) {
      case ELEMENT_TYPE.RADIO:
        return event.target.value;
      case ELEMENT_TYPE.CHECKBOX:
        return event.target.checked;
      default:
        return event.target.value;
    }
  }

  return (<MyReactElement handleChange={handleChange} {...props}  />)
}

export default FormElementListComponent;

