describe('The Home Page', () => {
  it('successfully loads', () => {
    cy.visit('/') // change URL to match your dev URL

    cy.get('h1').contains('Welcome to EnergyMap')
  })
})

