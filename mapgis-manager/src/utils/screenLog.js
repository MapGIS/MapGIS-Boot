/* eslint-disable */
export const printANSI = () => {
  // console.clear()
  console.log('[MapGIS Manager] created()')
  console.log('POWERED BY MapGIS')
  // ASCII - ANSI Shadow
  let text = `
  mmm  mmm                         mmmm    mmmmmm     mmmm   
  ###  ###                       ##""""#   ""##""   m#""""#  
  ########   m#####m  ##m###m   ##           ##     ##m      
  ## ## ##   " mmm##  ##"  "##  ##  mmmm     ##      "####m  
  ## "" ##  m##"""##  ##    ##  ##  ""##     ##          "## 
  ##    ##  ##mmm###  ###mm##"   ##mmm##   mm##mm   #mmmmm#" 
  ""    ""   """" ""  ## """       """"    """"""    """""   
                      ##                                       
`
  console.log(`%c${text}`, 'color: #fc4d50')
}
