import {ACTIONS} from "../constants/contants";
import set from 'lodash/fp/set'
import flow from 'lodash/fp/flow'
import filter from 'lodash/fp/filter'
import cloneDeep from 'lodash/cloneDeep'
import get from 'lodash/fp/get'

function formRenderReducer(state, action) {
  switch (action.type) {
    case ACTIONS.ON_CHANGE_RENDER:
      return {valueMap: set( action.id, action.value)(state.valueMap)};
    case ACTIONS.ON_ADD_ITEM_RENDER:
      if(action.id) {
        const id = (new Date().getTime()).toString().trim();
        const {valueMap = {}} = state
        const currentValue = valueMap[action.id] || [];
        return {...state, valueMap: set(action.id, currentValue.concat([{id }]))(state.valueMap)};
      }
      return state
    case ACTIONS.ON_REM_ITEM_RENDER:
      if(action.id && action.index >= 0){
        const currentValueMap = cloneDeep(state.valueMap)
        const currentValue = currentValueMap[action.id]
        const newValue = flow(set(`[${action.index}]`, null), filter(item => item !== null))(currentValue);
        return {...state, valueMap: set(action.id, newValue)(currentValueMap)};
      }
      return state
    case ACTIONS.ON_DRAG_END_ITEM_RENDER:
      if (!action.result.destination) {
        return state;
      }else {
        const currentValueMap = cloneDeep(state.valueMap)
        let currentValue = get(action.result.type)(currentValueMap)
        const source = currentValue.splice(action.result.source.index, 1)
        currentValue.splice(action.result.destination.index, 0, source[0])
        return {...state, valueMap: set(action.result.type, currentValue)(currentValueMap)};
      }
    default:
      throw new Error();
  }
}

export default formRenderReducer
