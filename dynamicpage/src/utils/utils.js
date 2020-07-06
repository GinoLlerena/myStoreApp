import { v4 as uuidv4 } from 'uuid'
import isObject from 'lodash/fp/isObject'
import isArray from 'lodash/fp/isArray'
import forEach from 'lodash/fp/forEach'
import map from 'lodash/fp/map'
import omit from 'lodash/fp/omit'
import reduce from 'lodash/fp/reduce'
import values from 'lodash/fp/values'
import flow from 'lodash/fp/flow'
import max from 'lodash/fp/max'
import filter from 'lodash/fp/filter'
import first from 'lodash/fp/first'

export function getLastLevel(elements){
  const level = flow(values, map(e => e.level), max)(elements)
  const lastElement = flow(values, filter( e => (e.level === level)), first)(elements)
  if(!!lastElement)
    return ({ [lastElement.key] : lastElement })
  else
    return undefined
}

export function getDescendents(el) {
  if (el) {
    if (isArray(el) && !(typeof el === 'string')) {
      return map(item => item.key)(el)
    } else if (isObject(el)) {
      return [el.key]
    }
  }
  return []
}

export function getPlainState(list, el){
  if(el !== null) {
    if(typeof  el !== 'string'){
      const children = (typeof el.children === 'string') ? el.children : undefined
      const data = {
        ...omit(['children'])(el),
        children,
        descendents: getDescendents(el.children)
      }
      list.push(data)
    }
    if (el.children) {
      if (isArray(el.children) && !(typeof el.children === 'string')) {
        forEach(item => getPlainState(list, item))(el.children)
      } else if (isObject(el.children)) {
        getPlainState(list, el.children)
      }
    }
  }
}

export function assignKeyToContent(el, level){
  if(el !== null) {
    if(typeof el !== 'string'){
      el.key = uuidv4()
      el.level = level
    }
    if (el.children) {
      if (isArray(el.children) && !(typeof el.children === 'string')) {
        forEach(item => assignKeyToContent(item, level + 1))(el.children)
      } else if (isObject(el.children)) {
        assignKeyToContent(el.children, level + 1)
      }
    }
  }
}

export function getDataSet(el) {
  return (el && el.dataset['key'] ? ({ key: el?.dataset['key'], level: el?.dataset['level'] }) : undefined)
}

export function getHoverKeys() {
  const elements = document.querySelectorAll(':hover')

  const hoveredElements = reduce((acc, el) => {
    const data = getDataSet(el)
    if (data) {
      acc[data.key] = data
    }
    return acc
  }, {})(elements)

  return hoveredElements
}
