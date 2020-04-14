import React, { useReducer} from "react"
import FormElementComponent from '../form/FormElementComponent'
import orderBy from 'lodash/fp/orderBy'
import flow from 'lodash/fp/flow'
import map from 'lodash/fp/map'
import form from '../templates/data'
import formRenderReducer from '../reducers/formRender'
import {ACTIONS} from '../constants/contants'
import {DragDropContext} from 'react-beautiful-dnd'

function ListElements(props){

  const { valueMap, setValue, addElement, remElement } = props;
  const { formElements } = form

  return flow(orderBy('displayOrder','asc'), map((element) => {
    return (
      <FormElementComponent key={element.elementId}
                            formElements={formElements}
                            element={element}
                            setValue={setValue}
                            valueMap={valueMap}
                            addElement={addElement}
                            remElement={remElement}
      />)
  }))(formElements)
}


function init() {
  return {valueMap: {
      relatives: [{id: '1', firstName: 'Juan', lastName: 'Lopez'}]
    }};
}

export default function FormManagement(props){

  const [state, dispatch] = useReducer(formRenderReducer, init());
  const setValue = (id, value) => dispatch({type: ACTIONS.ON_CHANGE_RENDER, id, value})
  const addItemListValue = id => dispatch({type: ACTIONS.ON_ADD_ITEM_RENDER, id})
  const remItemListValue = (id, index) => dispatch({type: ACTIONS.ON_REM_ITEM_RENDER, id, index})
  const dragEndListValue = result => dispatch({type: ACTIONS.ON_DRAG_END_ITEM_RENDER, result})

  return(
    <div className="container">
      <form>
        <div><pre>{JSON.stringify(state.valueMap, null, 2) }</pre></div>
        <DragDropContext onDragEnd={dragEndListValue}>
          <ListElements valueMap={state.valueMap} setValue={setValue} addElement={addItemListValue} remElement={remItemListValue}  />
        </DragDropContext>
        <div className="form-group" style={{paddingTop:'2em'}}>
          <div className="text-center">
            <button type="button" className="btn btn-success" onClick={()=> console.log('valueMap', state.valueMap)}>Send</button>
          </div>
        </div>
      </form>
    </div>
  )
}


