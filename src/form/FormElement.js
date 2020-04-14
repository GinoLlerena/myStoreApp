import React from 'react'
import map from 'lodash/fp/map'
import get from 'lodash/fp/get'
import entries from 'lodash/fp/entries'
import flow from 'lodash/fp/flow'
import orderBy from 'lodash/fp/orderBy'
import FormElementListComponent from './FormElementListComponent'
import {DraggableItem} from '../components/DragAndDrop/DraggableItem'
import {DDDroppable} from '../components/DragAndDrop/DDDroppable'

export const RadioOption = ({item, currentValue, handleChange, readOnly}) => {
  return (
    <div className="form-check">
      <input className="form-check-input" type="radio" id={item.elementvalueId} disabled={readOnly}
             value={item.elementvalueId} checked={item.elementvalueId === currentValue} onChange={handleChange}/>
      <label className="form-check-label" htmlFor={item.elementvalueId}>
        {item.displayName}
      </label>
    </div>
  )
}

function getOptionList(formElementValues){
  const list = formElementValues && formElementValues.length ? map( (item) => {
    return(
      <option key={item.elementvalueId} value={item.elementvalueId}>{item.displayName}</option>
    )
  })(formElementValues) : null;

  return list;
}

export const SimpleSelectElement = (props) => {

  const {valueMap, element, handleChange} = props;
  const {formElementValues} = element;

  return(
    <div className="form-group">
      <label htmlFor={element.elementId}>{element.displayName}</label>
      <select className="form-control" id={element.elementId} value={get(element.elementId)(valueMap)} disabled={element.readOnly} onChange={handleChange}>
        {getOptionList(formElementValues)}
      </select>
    </div>
  )
}

export const PasswordElement = (props) => {
  const {valueMap, element, handleChange} = props;
  return(
    <div className="form-group">
      <label htmlFor={element.elementId}>{element.displayName}</label>
      <input type="password" className="form-control" id={element.elementId} defaultValue={get(element.elementId)(valueMap)} placeholder="Password" onChange={handleChange} disabled={element.readOnly} />
    </div>
  )
}

export const CheckboxElement = (props) => {
  const {valueMap, element, handleChange} = props;

    return (
      <div className="form-check">
        <label className="form-check-label">
          <input type="checkbox" defaultChecked={get(element.elementId)(valueMap)} onChange={handleChange} disabled={element.readOnly} className="form-check-input"/> <span>{element.displayName}</span>
        </label>
      </div>
  )
}

export const PrintElement = (props) => {
  const {element} = props;
  return(
    <div className="form-group">
      <label htmlFor="formControlRange">{element.displayName}</label>
    </div>
  )
}



export const TextElement = (props) => {

  const {valueMap, element, handleChange} = props;

  return(
    <div className="form-group">
      <label htmlFor={element.elementId}>{element.displayName}</label>
      <input type="text"
             className="form-control"
             autoComplete="off"
             name={element.elementId}
             id={element.elementId}
             onChange={handleChange}
             defaultValue={get(element.elementId)(valueMap)}
             disabled={element.readOnly}
             required=""/>
      <div className="invalid-feedback">Please enter your username or email</div>
    </div>
  )
}

export const TextAreaElement = (props) => {

  const {valueMap, element, handleChange} = props;

  return(
    <div className="form-group">
      <label htmlFor={element.elementId}>{element.displayName}</label>
      <textarea className="form-control"
                id={element.elementId}
                defaultValue={get(element.elementId)(valueMap)}
                disabled={element.readOnly}
                onChange={handleChange}
                rows="3" />
    </div>
  )
}


export const RadioElement = (props) => {

  const {valueMap, element, handleChange} = props;
  const {formElementValues} = element;
  const currentValue = get(element.elementId)(valueMap);
  const list = formElementValues && formElementValues.length ? map((item) => <RadioOption key={item.elementvalueId} item={item}  currentValue={currentValue} handleChange={handleChange} readOnly={element.readOnly} />)(formElementValues) : null;

  return(
    <fieldset className="form-group">
      <div className="row">
        <legend className="col-form-label col-sm-2 pt-0">{element.displayName}</legend>
        <div className="col-sm-10">
          {list}
        </div>
      </div>
    </fieldset>
  )
}

export const ListElement = (props) => {
  const {valueMap, element, setValue, addElement, remElement} = props;
  const { internalForm, hiddenAdd } = element;
  const items = get(element.elementId)(valueMap)
  const list = flow(entries,
    map( ([index, valueMap]) => {
      const currentKey = `${element.elementId}-${valueMap.id}`
      return(
        <DraggableItem key={currentKey} draggableId={currentKey} index={parseInt(index)}>
          <div className="d-flex flex-row bd-highlight" >
          <div className="p-2 align-self-center">
            <i className="fas fa-bars" />
          </div>
          {flow(
                orderBy('displayOrder','asc'),
                map( innerElement => {
                  const elementKey = `${element.elementId}[${index}][${innerElement.elementId}]`
                  return (<div className="p-2 flex-fill bd-highlight" key={elementKey} >
                                <FormElementListComponent
                                                          elementKey={elementKey}
                                                          element={innerElement}
                                                          setValue={setValue}
                                                          valueMap={valueMap}
                                />
                      </div>)
                })
              )(internalForm)}
              <div className="p-2 flex-fill align-self-center">
                <i className="fas fa-trash" onClick={()=>remElement(element.elementId, index)} />
              </div>
          </div>
        </DraggableItem>)
      })
    )(items)


  return(
    <fieldset className="form-group">
      <div className="row" style={{paddingTop:'2em'}}>
        <legend className="col-form-label col-3 pt-0">
          <h5>{element.displayName}</h5>
        </legend>
        {list && list.length ?
          <div className="col-12">
            <DDDroppable droppableId={element.elementId} type={element.elementId}>
              {list}
            </DDDroppable>
          </div>: null}
      </div>
      { hiddenAdd ? null :
      <div className="row mt-2" >
        <div className="col text-left">
          <button type="button" className="btn btn-success" onClick={()=>addElement(element.elementId)}>Add</button>
        </div>
      </div>}
    </fieldset>
  )

}
