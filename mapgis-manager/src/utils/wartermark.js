const watermark = {}
const idd = Symbol('watermark-dom').toString()
let _interval = null

const setWatermark = str => {
  const id = idd
  if (document.getElementById(id) !== null) {
    document.body.removeChild(document.getElementById(id))
  }
  const can = document.createElement('canvas')
  can.width = 300
  can.height = 240
  const cans = can.getContext('2d')
  if (cans) {
    cans.rotate((-20 * Math.PI) / 120)
    cans.font = '15px Vedana'
    cans.fillStyle = 'rgba(0, 0, 0, 0.15)'
    cans.textAlign = 'left'
    cans.textBaseline = 'middle'
    cans.fillText(str, can.width / 20, can.height)
  }
  const div = document.createElement('div')
  div.id = id
  div.style.pointerEvents = 'none'
  div.style.top = '0px'
  div.style.left = '0px'
  div.style.position = 'absolute'
  div.style.zIndex = '100000'
  div.style.width = document.documentElement.clientWidth + 'px'
  div.style.height = document.documentElement.clientHeight + 'px'
  div.style.background = 'url(' + can.toDataURL('image/png') + ') left top repeat'
  document.body.appendChild(div)
  return id
}
// 该方法只允许调用一次
// 添加水印的方法
watermark.set = str => {
  let id = setWatermark(str)
  _interval = setInterval(() => {
    if (document.getElementById(id) === null) {
      id = setWatermark(str)
    }
  }, 500)
  window.onresize = () => {
    setWatermark(str)
  }
}
// 移除水印的方法
watermark.remove = () => {
  if (document.getElementById(idd) !== null) {
    var box = document.getElementById(idd)
    box.parentNode.removeChild(box)
    if (_interval) {
      clearInterval(_interval)
    }
  }
}
export default watermark
