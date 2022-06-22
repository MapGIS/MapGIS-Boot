const sidebar = {
  document: [
    '/guide/document/introduction',
    '/guide/document/frontend',
    '/guide/document/backend',
    '/guide/document/deploy',
    '/guide/document/skill'
  ],
  other: ['/guide/other/change-log', '/guide/other/faq']
}

function getSidebarByCategory(category, lang = 'en') {
  const links = JSON.parse(JSON.stringify(sidebar[category])) // Deep clone
  return links.map(link => {
    if (lang != 'en' && link.startsWith('/')) {
      return `/${lang}${link}`
    }
    return link
  })
}

module.exports = {
  getSidebarByCategory
}
